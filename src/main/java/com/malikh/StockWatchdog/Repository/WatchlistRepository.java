package com.malikh.StockWatchdog.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.malikh.StockWatchdog.Entity.WatchlistEntry;

@Repository
public interface WatchlistRepository extends JpaRepository<WatchlistEntry, Long> {
    
}
