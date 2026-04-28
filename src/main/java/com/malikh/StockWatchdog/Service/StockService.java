package com.malikh.StockWatchdog.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.malikh.StockWatchdog.Entity.Stock;
import com.malikh.StockWatchdog.Repository.StockRepository;

@Service
public class StockService {
    @Autowired
    public StockRepository stockRepo;
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
    public void updateStock(String s, Stock st){
        st.setSymbol(s);
        stockRepo.save(st);
    }
    //DELETE
    public void deleteStock(String s){
        stockRepo.deleteById(s);
    }

}
