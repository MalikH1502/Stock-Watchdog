package com.malikh.stockwatchdog.controllers;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.malikh.stockwatchdog.entity.WatchlistEntry;
import com.malikh.stockwatchdog.service.WatchlistService;

@RestController
@RequestMapping("/api")
public class WatchlistController {
    private WatchlistService watchService;
    public WatchlistController(WatchlistService watchService) {
        this.watchService = watchService;
    }

    // Add stock to watchlist
    @PostMapping("/watchlist")
    public void addToWatchlist(@RequestBody WatchlistEntry w) {
        watchService.addToWatchlist(w);
    }
}
