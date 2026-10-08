package com.malikh.stockwatchdog.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.malikh.stockwatchdog.dto.WatchlistAddRequest;
import com.malikh.stockwatchdog.service.WatchlistService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class WatchlistController {
    private WatchlistService watchService;

    public WatchlistController(WatchlistService watchService) {
        this.watchService = watchService;
    }

    // Add stock to watchlist
    @PostMapping("/watchlist")
    public void addToWatchlist(@Valid @RequestBody WatchlistAddRequest request, Authentication authentication) {
        watchService.addToWatchlist(request.getStockId(), authentication.getName());
    }
}
