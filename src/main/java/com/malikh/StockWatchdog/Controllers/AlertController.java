package com.malikh.stockwatchdog.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.malikh.stockwatchdog.entity.Alert;
import com.malikh.stockwatchdog.service.AlertService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AlertController {
    private AlertService alertService;
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    // Create Alert
    @PostMapping("/alerts")
    public void createAlert(@RequestBody Alert alert) {
        alertService.createAlert(alert);
    }

    
    
}
