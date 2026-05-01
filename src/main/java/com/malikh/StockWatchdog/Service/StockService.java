package com.malikh.stockwatchdog.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.malikh.stockwatchdog.dto.StockDTO;
import com.malikh.stockwatchdog.entity.Stock;
import com.malikh.stockwatchdog.repository.StockRepository;
import com.mapper.StockMapper;

@Service
public class StockService {
    private final StockRepository stockRepo;
    private final StockMapper stockMapper;

    public StockService(StockRepository stockRepo, StockMapper stockMapper) {
        this.stockRepo = stockRepo;
        this.stockMapper = stockMapper;
    }

    // CRUD
    // Create
    public void createStock(Stock s){
        stockRepo.save(s);
    }
    //READ
    public List<StockDTO> getAllStocks(){
        return stockRepo.findAll().stream()
                .map(stockMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<StockDTO> findById(String s){
        return stockRepo.findById(s).map(stockMapper::toDTO);
    }
    //UPDATE
    public StockDTO updateStock(String s, Stock updatedStock){
        Stock existingStock = stockRepo.findById(s)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        existingStock.setCompanyName(updatedStock.getCompanyName());
        existingStock.setPrice(updatedStock.getPrice());
        return stockMapper.toDTO(stockRepo.save(existingStock));
    }
    //DELETE
    public void deleteStock(String s){
        if (!stockRepo.existsById(s)) {
            throw new RuntimeException("Stock not found");
        }
        stockRepo.deleteById(s);
    }

}
