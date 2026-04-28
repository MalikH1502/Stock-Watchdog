package com.malikh.StockWatchdog.Service;

import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.malikh.StockWatchdog.Entity.Stock;
import com.malikh.StockWatchdog.Repository.StockRepository;

@Service
public class StockService {
    private final StockRepository stockRepo;

    public StockService(StockRepository stockRepo) {
        this.stockRepo = stockRepo;
    }

    // CRUD
    // Create
    public void createStock(Stock s){
        stockRepo.save(s);
    }
    //READ
    public List<Stock> getAllStocks(){
        return stockRepo.findAll();
    }
    
    public Optional<Stock> findById(String s){
        return stockRepo.findById(s);
    }
    //UPDATE
    public Stock updateStock(String s, Stock updatedStock){
        Stock existingStock = stockRepo.findById(s)
                .orElseThrow(() -> new RuntimeErrorException("Stock not found"));
        existingStock.setCompanyName(updatedStock.getCompanyName());
        existingStock.setPrice(updatedStock.getPrice());
        return stockRepo.save(existingStock);
    }
    //DELETE
    public void deleteStock(String s){
        if (!stockRepo.existsById(s)) {
            throw new RuntimeErrorException("Stock not found");
        }
        stockRepo.deleteById(s);
    }

}
