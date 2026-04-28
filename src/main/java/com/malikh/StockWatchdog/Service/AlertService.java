package com.malikh.StockWatchdog.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.malikh.StockWatchdog.Entity.Alert;
import com.malikh.StockWatchdog.Repository.AlertRepository;

@Service
public class AlertService {
    private final AlertRepository alertRepo;

    public AlertService(AlertRepository alertRepo) {
        this.alertRepo = alertRepo;
    }

    // Create
    public Alert createAlert(Alert a) {
        return alertRepo.save(a);
    }

    // Read
    public List<Alert> getAllAlerts() {
        return alertRepo.findAll();
    }

    // Update
    public Alert updateAlert(Long id, Alert updatedAlert) {
        Alert existingAlert = alertRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found with id: " + id));

        existingAlert.setStock(updatedAlert.getStock());
        existingAlert.setTargetPrice(updatedAlert.getValue());

        return alertRepo.save(updatedAlert);
    }

    // Delete
    public void deleteAlert(Long id) {
        if (!alertRepo.existsById(id)) {
            throw new RuntimeException("Alert not found with id: " + id);
        }

        alertRepo.deleteById(id);
    }
}
