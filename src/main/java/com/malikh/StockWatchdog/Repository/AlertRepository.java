package com.malikh.StockWatchdog.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.malikh.StockWatchdog.Entity.Alert;
@Repository
public interface AlertRepository extends JpaRepository<Alert, Long>{
    
}
