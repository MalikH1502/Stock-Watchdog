package com.malikh.stockwatchdog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.malikh.stockwatchdog.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    
}
