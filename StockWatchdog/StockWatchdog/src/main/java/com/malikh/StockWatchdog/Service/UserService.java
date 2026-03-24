package com.malikh.StockWatchdog.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.malikh.StockWatchdog.Entity.User;
import com.malikh.StockWatchdog.Repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private static UserRepository userRepo;

    public static void createUser(User u){
        userRepo.save(u);
    }
}
