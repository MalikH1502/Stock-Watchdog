package com.malikh.stockwatchdog.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.malikh.stockwatchdog.dto.AlertCreateRequest;
import com.malikh.stockwatchdog.dto.AlertDTO;
import com.malikh.stockwatchdog.entity.Alert;
import com.malikh.stockwatchdog.entity.Stock;
import com.malikh.stockwatchdog.entity.User;
import com.malikh.stockwatchdog.exception.ForbiddenOperationException;
import com.malikh.stockwatchdog.exception.ResourceNotFoundException;
import com.malikh.stockwatchdog.mapper.AlertMapper;
import com.malikh.stockwatchdog.repository.AlertRepository;
import com.malikh.stockwatchdog.repository.StockRepository;
import com.malikh.stockwatchdog.repository.UserRepository;

@Service
public class AlertService {
    private final AlertMapper alertMapper;
    private final AlertRepository alertRepo;
    private final StockRepository stockRepo;
    private final UserRepository userRepo;

    public AlertService(AlertRepository alertRepo, AlertMapper alertMapper,
            StockRepository stockRepo, UserRepository userRepo) {
        this.alertRepo = alertRepo;
        this.alertMapper = alertMapper;
        this.stockRepo = stockRepo;
        this.userRepo = userRepo;
    }

    // Create, scoped to the requesting user
    public AlertDTO createAlert(AlertCreateRequest request, String username) {
        Stock stock = stockRepo.findById(request.getStockId())
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found with id: " + request.getStockId()));
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Alert alert = new Alert();
        alert.setCondition(request.getCondition());
        alert.setValue(request.getValue());
        alert.setStock(stock);
        alert.setUser(user);
        alert.setIsTrue(false); // no evaluation logic exists yet, so this just means "not yet checked"

        Alert savedAlert = alertRepo.save(alert);
        return alertMapper.toDTO(savedAlert);
    }

    // Read, scoped to the requesting user
    public List<AlertDTO> getAlertsForUser(String username) {
        return alertRepo.findByUserUsername(username).stream()
                .map(alertMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Update
    public AlertDTO updateAlert(Long id, Alert updatedAlert, String username) {
    Alert existingAlert = alertRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + id));

    if (!existingAlert.getUser().getUsername().equals(username)) {
        throw new ForbiddenOperationException("Alert does not belong to this user");
    }

    existingAlert.setStock(updatedAlert.getStock());
    existingAlert.setValue(updatedAlert.getValue());
    Alert savedAlert = alertRepo.save(existingAlert);
    return alertMapper.toDTO(savedAlert);
}

    public AlertDTO markFired(Long id, String username) {
        Alert alert = alertRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + id));

        if (!alert.getUser().getUsername().equals(username)) {
            throw new ResourceNotFoundException("Alert does not belong to this user");
        }

        alert.setIsTrue(true);
        Alert saved = alertRepo.save(alert);
        return alertMapper.toDTO(saved);
    }

    // Delete
    public void deleteAlert(Long id, String username) {
        Alert alert = alertRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + id));

        if (!alert.getUser().getUsername().equals(username)) {
            throw new ResourceNotFoundException("Alert does not belong to this user");
        }

        alertRepo.deleteById(id);
    }
}