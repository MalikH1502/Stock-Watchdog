package com.malikh.stockwatchdog.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.malikh.stockwatchdog.dto.UserDTO;
import com.malikh.stockwatchdog.entity.User;
import com.malikh.stockwatchdog.repository.UserRepository;
import com.mapper.UserMapper;

@Service
public class UserService {
    
    private final UserRepository userRepo;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepo, UserMapper userMapper){
        this.userRepo = userRepo;
        this.userMapper = userMapper;
    }

    public void createUser(User u){
        userRepo.save(u);
    }

    public List<UserDTO> getAllUsers(){
        return userRepo.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO updateUser(Long id, User updatedUser){
        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(updatedUser.getPassword());

        return userMapper.toDTO(userRepo.save(existingUser));
    }

    public void deleteUser(Long id){
        if (!userRepo.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        
        userRepo.deleteById(id);
    }
}
