package com.malikh.stockwatchdog.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ssl.SslProperties.Bundles.Watch;
import org.springframework.stereotype.Service;

import com.malikh.stockwatchdog.dto.WatchlistEntryDTO;
import com.malikh.stockwatchdog.entity.WatchlistEntry;
import com.malikh.stockwatchdog.mapper.WatchlistEntryMapper;
import com.malikh.stockwatchdog.repository.WatchlistRepository;

@Service
public class WatchlistService {
    private final WatchlistRepository watchlistRepo;
    private final WatchlistEntryMapper watchlistEntryMapper;

    public WatchlistService(WatchlistRepository watchlistRepo, WatchlistEntryMapper watchlistEntryMapper) {
        this.watchlistRepo = watchlistRepo;
        this.watchlistEntryMapper = watchlistEntryMapper;
    }

    public void addToWatchlist(WatchlistEntry e) {
        watchlistRepo.save(e);
    }

    public List<WatchlistEntryDTO> getAllWatchlistEntries() {
        return watchlistRepo.findAll().stream()
                .map(watchlistEntryMapper::toDTO)
                .collect(Collectors.toList());
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
