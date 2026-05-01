package com.malikh.stockwatchdog.service;

import com.mapper.AlertMapper;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.malikh.stockwatchdog.dto.AlertDTO;
import com.malikh.stockwatchdog.entity.Alert;
import com.malikh.stockwatchdog.repository.AlertRepository;

@Service
public class AlertService {
    private final AlertMapper alertMapper;
    private final AlertRepository alertRepo;

    public AlertService(AlertRepository alertRepo, AlertMapper alertMapper) {
        this.alertRepo = alertRepo;
        this.alertMapper = alertMapper;
    }

    // Create
    public AlertDTO createAlert(Alert a) {
        Alert savedAlert = alertRepo.save(a);
        return alertMapper.toDTO(savedAlert);
    }

    // Read
    public List<AlertDTO> getAllAlerts() {
        return alertRepo.findAll().stream()
                .map(alertMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Update
    public AlertDTO updateAlert(Long id, Alert updatedAlert) {
        Alert existingAlert = alertRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found with id: " + id));

        existingAlert.setStock(updatedAlert.getStock());
        existingAlert.setValue(updatedAlert.getValue());
        Alert savedAlert = alertRepo.save(existingAlert);
        return alertMapper.toDTO(savedAlert);
    }

    // Delete
    public void deleteAlert(Long id) {
        if (!alertRepo.existsById(id)) {
            throw new RuntimeException("Alert not found with id: " + id);
        }

        alertRepo.deleteById(id);
    }
}
