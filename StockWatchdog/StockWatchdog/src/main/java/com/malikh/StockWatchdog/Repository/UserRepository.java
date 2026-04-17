package com.malikh.StockWatchdog.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.malikh.StockWatchdog.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    
}
