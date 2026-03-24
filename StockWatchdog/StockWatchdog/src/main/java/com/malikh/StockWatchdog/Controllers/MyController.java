package com.malikh.StockWatchdog.Controllers;

import java.util.List;
import java.util.Optional;

import com.malikh.StockWatchdog.Entity.*;
import com.malikh.StockWatchdog.Repository.AlertRepository;
import com.malikh.StockWatchdog.Repository.StockRepository;
import com.malikh.StockWatchdog.Repository.WatchlistRepository;
import com.malikh.StockWatchdog.Service.AlertService;
import com.malikh.StockWatchdog.Service.StockService;
import com.malikh.StockWatchdog.Service.UserService;
import com.malikh.StockWatchdog.Service.WatchlistService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MyController {

    @Autowired
    private StockService stockService;
    @Autowired
    private WatchlistService watchService;
    @Autowired
    private AlertService alertService;
    @Autowired
    private UserService userService;

    // Return all stocks
    @GetMapping("/stocks")
    public List<Stock> retrieveStockList() {
        return stockService.getAllStocks();
    }

    // Return SPECIFIC stock
    @GetMapping("/stocks/{symbol}")
    public Optional<Stock> findByStock(@PathVariable String symbol) {
        return stockService.findById(symbol);
    }

    @PostMapping("/stocks")
    public void createStock(@PathVariable Stock stock){
        stockService.createStock(stock);
    }

    // Add stock to watchlist
    @PostMapping("/watchlist")
    public void addToWatchlist(@RequestBody WatchlistEntry w) {
        watchService.addToWatchlist(w);
    }

    // Create Alert
    @PostMapping("/alerts")
    public void createAlert(@RequestBody Alert alert) {
        alertService.createAlert(alert);
    }

    @PostMapping("/users"){
        public void createUser(@RequestBody User u){
            UserService.createUser(u);
        }
    }
}
