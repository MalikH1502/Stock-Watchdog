package com.malikh.StockWatchdog.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.malikh.StockWatchdog.Entity.User;
import com.malikh.StockWatchdog.Repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    public void createUser(User u){
        userRepo.save(u);
    }

    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    public User updateUser(Long id, User updatedUser){
        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(updatedUser.getPassword());

        return userRepo.save(existingUser);
    }

    public void deleteUser(Long id){
        if (!userRepo.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        
        userRepo.deleteById(id);
    }
}
