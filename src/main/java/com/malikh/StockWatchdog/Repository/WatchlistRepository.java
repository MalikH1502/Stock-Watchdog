package com.malikh.stockwatchdog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.malikh.stockwatchdog.entity.WatchlistEntry;

@Repository
public interface WatchlistRepository extends JpaRepository<WatchlistEntry, Long> {
    
}
