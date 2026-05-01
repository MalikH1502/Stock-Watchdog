package com.malikh.stockwatchdog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.malikh.stockwatchdog.entity.Alert;
@Repository
public interface AlertRepository extends JpaRepository<Alert, Long>{
    
}
