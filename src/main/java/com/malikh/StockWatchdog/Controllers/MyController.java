package com.malikh.stockwatchdog.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.malikh.stockwatchdog.repository.UserRepository;
import com.malikh.stockwatchdog.entity.Alert;
import com.malikh.stockwatchdog.entity.Stock;
import com.malikh.stockwatchdog.entity.User;
import com.malikh.stockwatchdog.entity.Stock;
import com.malikh.stockwatchdog.entity.WatchlistEntry;
import com.malikh.stockwatchdog.repository.AlertRepository;
import com.malikh.stockwatchdog.repository.StockRepository;
import com.malikh.stockwatchdog.repository.WatchlistRepository;
import com.malikh.stockwatchdog.service.AlertService;
import com.malikh.stockwatchdog.service.StockService;
import com.malikh.stockwatchdog.service.UserService;
import com.malikh.stockwatchdog.service.WatchlistService;

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

    @PostMapping("/users")
        public void createUser(@RequestBody User u){
            userService.createUser(u);
        }
    

    @PostMapping("/stocks")
        public void createStock(@RequestBody Stock s){
            stockService.createStock(s);
        }
    

    @DeleteMapping("/stocks/{symbol}")
        public void deleteStockById(@PathVariable String symbol){
            stockService.deleteStock(symbol);}
        


    @PutMapping("/stocks/{symbol}")
        public void updateStock(@PathVariable String symbol, @RequestBody Stock s){
            stockService.updateStock(symbol, s);
        }
    }