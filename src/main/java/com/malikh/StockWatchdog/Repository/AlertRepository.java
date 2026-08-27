package com.malikh.stockwatchdog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.malikh.stockwatchdog.entity.Alert;
@Repository
public interface AlertRepository extends JpaRepository<Alert, Long>{
    List<Alert> findByUserUsername(String username);
    boolean existsByStockId(Long stockId);
}