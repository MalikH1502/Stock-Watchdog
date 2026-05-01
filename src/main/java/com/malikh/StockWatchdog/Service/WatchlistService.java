package com.malikh.stockwatchdog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.malikh.stockwatchdog.entity.WatchlistEntry;
import com.malikh.stockwatchdog.repository.WatchlistRepository;

@Service
public class WatchlistService {
    private final WatchlistRepository watchlistRepo;

    public WatchlistService(WatchlistRepository watchlistRepo) {
        this.watchlistRepo = watchlistRepo;
    }

    public void addToWatchlist(WatchlistEntry e) {
        watchlistRepo.save(e);
    }

    public List<WatchlistEntry> getAllWatchlistEntries() {
        return watchlistRepo.findAll();
    }

    public WatchlistEntry updateWatchlistEntry(Long id, WatchlistEntry updatedEntry) {
        WatchlistEntry existingEntry = watchlistRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Watchlist entry not found with id: " + id));

        existingEntry.setStock(updatedEntry.getStock());
        existingEntry.setUser(updatedEntry.getUser());

        return watchlistRepo.save(existingEntry);
    }

    public void deleteWatchlistEntry(Long id) {
        if (!watchlistRepo.existsById(id)) {
            throw new RuntimeException("Watchlist entry not found with id: " + id);
        }

        watchlistRepo.deleteById(id);
    }

}
