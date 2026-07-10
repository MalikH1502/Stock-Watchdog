package com.malikh.stockwatchdog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.malikh.stockwatchdog.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    public Optional<Stock> findStockBySymbol(String symbol);
    
}
