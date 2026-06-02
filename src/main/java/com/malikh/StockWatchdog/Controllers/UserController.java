package com.malikh.stockwatchdog.controllers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.malikh.stockwatchdog.dto.LoginRequest;
import com.malikh.stockwatchdog.dto.UserDTO;
import com.malikh.stockwatchdog.dto.UserRegistrationRequest;
import com.malikh.stockwatchdog.entity.User;
import com.malikh.stockwatchdog.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController {
    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationRequest request) {
        userService.createUser(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        UserDTO user = userService.login(request);
        return ResponseEntity.ok(user);
    }
}
