package com.malikh.stockwatchdog.controllers;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
public class UserController {
    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/csrf")
    public Map<String, String> csrf(CsrfToken token) {
        return Map.of("token", token.getToken());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid UserRegistrationRequest request) {
        userService.createUser(request);
        return ResponseEntity.ok("User registered successfully");
    }

}
