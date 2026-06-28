package com.malikh.stockwatchdog.controllers;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.malikh.stockwatchdog.dto.StockDTO;
import com.malikh.stockwatchdog.entity.Stock;
import com.malikh.stockwatchdog.service.StockService;

@RestController
@RequestMapping("/api")
public class StockController {
    private StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // Return all stocks
    @GetMapping("/stocks")
    public List<StockDTO> retrieveStockList() {
        return stockService.getAllStocks();
    }

    // Return specific stock
    @GetMapping("/stocks/{id}")
    public Optional<StockDTO> findByStock(@PathVariable Long id) {
        return stockService.findById(id);
    }

    // Create Stock
    @PostMapping("/stocks")
        public Stock createStock(@RequestBody Stock s){
            stockService.createStock(s);
            return s;   
        }

    //Delete Stock
    @DeleteMapping("/stocks/{id}")
        public void deleteStockById(@PathVariable Long id){
            stockService.deleteStock(id);}


    //Update Stock
    @PutMapping("/stocks/{id}")
        public void updateStock(@PathVariable Long id, @RequestBody Stock s){
            stockService.updateStock(id, s);
        }

}
