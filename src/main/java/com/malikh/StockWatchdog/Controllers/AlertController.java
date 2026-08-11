package com.malikh.stockwatchdog.controllers;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.malikh.stockwatchdog.dto.AlertCreateRequest;
import com.malikh.stockwatchdog.dto.AlertDTO;
import com.malikh.stockwatchdog.service.AlertService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AlertController {
    private AlertService alertService;
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping("/alerts")
    public AlertDTO createAlert(@Valid @RequestBody AlertCreateRequest request, Authentication authentication) {
        return alertService.createAlert(request, authentication.getName());
    }

    @GetMapping("/alerts")
    public List<AlertDTO> getMyAlerts(Authentication authentication) {
        return alertService.getAlertsForUser(authentication.getName());
    }

    @PostMapping("/alerts/{id}/mark-fired")
    public AlertDTO markFired(@PathVariable Long id, Authentication authentication) {
        return alertService.markFired(id, authentication.getName());
    }
}