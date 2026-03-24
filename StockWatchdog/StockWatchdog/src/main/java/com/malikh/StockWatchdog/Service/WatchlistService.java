package com.malikh.StockWatchdog.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.malikh.StockWatchdog.Entity.WatchlistEntry;
import com.malikh.StockWatchdog.Repository.WatchlistRepository;

@Service
public class WatchlistService {
    @Autowired
    private WatchlistRepository watchlistRepo;

    public void addToWatchlist(WatchlistEntry e){
        watchlistRepo.save(e);
    }
    
}
