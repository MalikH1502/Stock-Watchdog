package com.malikh.stockwatchdog.service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.malikh.stockwatchdog.dto.AlphaVantageMatch;
import com.malikh.stockwatchdog.dto.StockDTO;
import com.malikh.stockwatchdog.entity.Stock;
import com.malikh.stockwatchdog.exception.ResourceInUseException;
import com.malikh.stockwatchdog.exception.ResourceNotFoundException;
import com.malikh.stockwatchdog.mapper.StockMapper;
import com.malikh.stockwatchdog.repository.AlertRepository;
import com.malikh.stockwatchdog.repository.StockRepository;
import com.malikh.stockwatchdog.repository.WatchlistRepository;

@Service
public class StockService {
    private final StockRepository stockRepo;
    private final StockMapper stockMapper;
    private final AlphaVantageService alphaVantageService;
    private final AlertRepository alertRepo;
    private final WatchlistRepository watchlistRepo;
    private static final Duration QUOTE_STALE_THRESHOLD = Duration.ofMinutes(5);

    public StockService(StockRepository stockRepo, StockMapper stockMapper,
            AlphaVantageService alphaVantageService, AlertRepository alertRepo,
            WatchlistRepository watchlistRepo) {
        this.stockRepo = stockRepo;
        this.stockMapper = stockMapper;
        this.alphaVantageService = alphaVantageService;
        this.alertRepo = alertRepo;
        this.watchlistRepo = watchlistRepo;
    }

    public long getTotalTrackedCount() {
        return stockRepo.countByFirstViewedAtIsNotNull();
    }

    public Optional<StockDTO> getStockDetail(Long id) {
        Optional<Stock> optionalStock = stockRepo.findById(id);
        if (optionalStock.isEmpty()) {
            return Optional.empty();
        }

        Stock stock = optionalStock.get();
        boolean isStale = stock.getLastUpdated() == null
                || Instant.now().isAfter(stock.getLastUpdated().plus(QUOTE_STALE_THRESHOLD));

        if (isStale) {
            Double freshPrice = alphaVantageService.getQuotePrice(stock.getSymbol());
            if (freshPrice != null) {
                stock.setPreviousPrice(stock.getPrice());
                stock.setPrice(freshPrice);
                stock.setLastUpdated(Instant.now());
            }
            // if freshPrice is null (rate limited, bad symbol, etc), we just keep serving
            // the old cached values
        }

        if (stock.getFirstViewedAt() == null) {
            stock.setFirstViewedAt(Instant.now());
        }

        stockRepo.save(stock);

        return Optional.of(stockMapper.toDTO(stock));
    }

    public List<StockDTO> upsertStock(String symbol) {
        List<AlphaVantageMatch> matches = alphaVantageService.searchSymbol(symbol);
        List<StockDTO> results = new ArrayList<StockDTO>();
        for (int i = 0; i < matches.size(); i++) {
            Optional<Stock> existing = stockRepo.findStockBySymbol(matches.get(i).getSymbol());
            if (existing.isEmpty()) {
                Stock s = new Stock();
                s.setSymbol(matches.get(i).getSymbol());
                s.setCompanyName(matches.get(i).getName());
                s.setType(matches.get(i).getType());
                s.setRegion(matches.get(i).getRegion());
                stockRepo.save(s);
                results.add(stockMapper.toDTO(s));
            } else {
                results.add(stockMapper.toDTO(existing.get()));

            }

        }
        return results;
    }

    // CRUD
    // Create
    public void createStock(Stock s) {
        stockRepo.save(s);
    }

    // READ
    public List<StockDTO> getAllStocks() {
        return stockRepo.findAll().stream()
                .map(stockMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<StockDTO> findById(Long s) {
        return stockRepo.findById(s).map(stockMapper::toDTO);
    }

    // UPDATE
    public StockDTO updateStock(Long s, Stock updatedStock) {
        Stock existingStock = stockRepo.findById(s)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found"));
        existingStock.setCompanyName(updatedStock.getCompanyName());
        existingStock.setPrice(updatedStock.getPrice());
        return stockMapper.toDTO(stockRepo.save(existingStock));
    }

    // DELETE
    public void deleteStock(Long s) {
        if (!stockRepo.existsById(s)) {
            throw new ResourceNotFoundException("Stock not found");
        }
        if (alertRepo.existsByStockId(s)) {
            throw new ResourceInUseException("Cannot delete stock: one or more alerts reference it");
        }
        if (watchlistRepo.existsByStockId(s)) {
            throw new ResourceInUseException("Cannot delete stock: it is present in a watchlist");
        }
        stockRepo.deleteById(s);
    }

}
