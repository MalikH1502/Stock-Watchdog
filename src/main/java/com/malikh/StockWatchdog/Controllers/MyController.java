package com.malikh.stockwatchdog.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.malikh.stockwatchdog.repository.UserRepository;
import com.malikh.stockwatchdog.dto.StockDTO;
import com.malikh.stockwatchdog.entity.Alert;
import com.malikh.stockwatchdog.entity.Stock;
import com.malikh.stockwatchdog.entity.User;
import com.malikh.stockwatchdog.entity.Stock;
import com.malikh.stockwatchdog.entity.WatchlistEntry;
import com.malikh.stockwatchdog.mapper.StockMapper;
import com.malikh.stockwatchdog.repository.AlertRepository;
import com.malikh.stockwatchdog.repository.StockRepository;
import com.malikh.stockwatchdog.repository.WatchlistRepository;
import com.malikh.stockwatchdog.service.AlertService;
import com.malikh.stockwatchdog.service.StockService;
import com.malikh.stockwatchdog.service.UserService;
import com.malikh.stockwatchdog.service.WatchlistService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MyController {
    private StockService stockService;
    private WatchlistService watchService;
    private AlertService alertService;
    private UserService userService;
    private StockMapper stockMapper;

    public MyController(StockService stockService, WatchlistService watchService, AlertService alertService, UserService userService, StockMapper stockMapper) {
        this.stockService = stockService;
        this.watchService = watchService;
        this.alertService = alertService;
        this.userService = userService;
        this.stockMapper = stockMapper;
    }
 

    // Return all stocks
    @GetMapping("/stocks")
    public List<StockDTO> retrieveStockList() {
        return stockService.getAllStocks();
    }

    // Return SPECIFIC stock
    @GetMapping("/stocks/{id}")
    public Optional<StockDTO> findByStock(@PathVariable Long id) {
        return stockService.findById(id);
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
        public Stock createStock(@RequestBody Stock s){
            stockService.createStock(s);
            return s;   
        }
    

    @DeleteMapping("/stocks/{id}")
        public void deleteStockById(@PathVariable Long id){
            stockService.deleteStock(id);}
        


    @PutMapping("/stocks/{id}")
        public void updateStock(@PathVariable Long id, @RequestBody Stock s){
            stockService.updateStock(id, s);
        }
    }