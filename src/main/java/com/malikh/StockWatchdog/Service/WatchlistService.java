package com.malikh.stockwatchdog.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.malikh.stockwatchdog.dto.WatchlistEntryDTO;
import com.malikh.stockwatchdog.entity.Stock;
import com.malikh.stockwatchdog.entity.User;
import com.malikh.stockwatchdog.entity.WatchlistEntry;
import com.malikh.stockwatchdog.exception.ResourceNotFoundException;
import com.malikh.stockwatchdog.mapper.WatchlistEntryMapper;
import com.malikh.stockwatchdog.repository.StockRepository;
import com.malikh.stockwatchdog.repository.UserRepository;
import com.malikh.stockwatchdog.repository.WatchlistRepository;

@Service
public class WatchlistService {
    private final WatchlistRepository watchlistRepo;
    private final WatchlistEntryMapper watchlistEntryMapper;
    private final StockRepository stockRepo;
    private final UserRepository userRepo;

    @Autowired
    public WatchlistService(WatchlistRepository watchlistRepo,
            WatchlistEntryMapper watchlistEntryMapper,
            StockRepository stockRepo,
            UserRepository userRepo) {
        this.watchlistRepo = watchlistRepo;
        this.watchlistEntryMapper = watchlistEntryMapper;
        this.stockRepo = stockRepo;
        this.userRepo = userRepo;
    }

    // WatchlistService.java — replace addToWatchlist
    public void addToWatchlist(Long stockId, String username) {
        Stock stock = stockRepo.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found with id: " + stockId));
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        WatchlistEntry entry = new WatchlistEntry();
        entry.setStock(stock);
        entry.setUser(user);
        entry.setDateAdded(LocalDate.now());
        watchlistRepo.save(entry);
    }

    public List<WatchlistEntryDTO> getAllWatchlistEntries() {
        return watchlistRepo.findAll().stream()
                .map(watchlistEntryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public WatchlistEntry updateWatchlistEntry(Long id, WatchlistEntry updatedEntry) {
        WatchlistEntry existingEntry = watchlistRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist entry not found with id: " + id));

        existingEntry.setStock(updatedEntry.getStock());
        existingEntry.setUser(updatedEntry.getUser());

        return watchlistRepo.save(existingEntry);
    }

    public void deleteWatchlistEntry(Long id) {
        if (!watchlistRepo.existsById(id)) {
            throw new ResourceNotFoundException("Watchlist entry not found with id: " + id);
        }

        watchlistRepo.deleteById(id);
    }

}
