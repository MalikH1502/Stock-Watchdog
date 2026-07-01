package com.malikh.stockwatchdog.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.malikh.stockwatchdog.dto.LoginRequest;
import com.malikh.stockwatchdog.dto.UserDTO;
import com.malikh.stockwatchdog.dto.UserRegistrationRequest;
import com.malikh.stockwatchdog.entity.User;
import com.malikh.stockwatchdog.mapper.UserMapper;
import com.malikh.stockwatchdog.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO createUser(UserRegistrationRequest request){
        User u = new User();
        u.setUsername(request.getUsername());
        u.setPassword(passwordEncoder.encode(request.getPassword()));
        u.setEmail(request.getEmail());

        return userMapper.toDTO(userRepo.save(u));
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
        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        existingUser.setEmail(updatedUser.getEmail());

        return userMapper.toDTO(userRepo.save(existingUser));
    }

    public void deleteUser(Long id){
        if (!userRepo.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        
        userRepo.deleteById(id);
    }
}
