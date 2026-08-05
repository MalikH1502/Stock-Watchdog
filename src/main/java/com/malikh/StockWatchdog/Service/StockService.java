package com.malikh.stockwatchdog.service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.malikh.stockwatchdog.dto.AlphaVantageMatch;
import com.malikh.stockwatchdog.dto.StockDTO;
import com.malikh.stockwatchdog.entity.Stock;
import com.malikh.stockwatchdog.mapper.StockMapper;
import com.malikh.stockwatchdog.repository.StockRepository;

@Service
public class StockService {
    private final StockRepository stockRepo;
    private final StockMapper stockMapper;
    private final AlphaVantageService alphaVantageService;
    private static final Duration QUOTE_STALE_THRESHOLD = Duration.ofMinutes(5);

    public StockService(StockRepository stockRepo, StockMapper stockMapper, AlphaVantageService alphaVantageService) {
        this.stockRepo = stockRepo;
        this.stockMapper = stockMapper;
        this.alphaVantageService = alphaVantageService;
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
                stock.setPrice(freshPrice);
                stock.setLastUpdated(Instant.now());
                stockRepo.save(stock);
            }
            // if freshPrice is null (rate limited, bad symbol, etc), we just keep serving
            // the old cached values
        }

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
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        existingStock.setCompanyName(updatedStock.getCompanyName());
        existingStock.setPrice(updatedStock.getPrice());
        return stockMapper.toDTO(stockRepo.save(existingStock));
    }

    // DELETE
    public void deleteStock(Long s) {
        if (!stockRepo.existsById(s)) {
            throw new RuntimeException("Stock not found");
        }
        stockRepo.deleteById(s);
    }

}
