package com.malikh.StockWatchdog.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.malikh.StockWatchdog.Entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, String> {
    
}
