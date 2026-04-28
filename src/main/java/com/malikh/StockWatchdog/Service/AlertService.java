package com.malikh.StockWatchdog.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.malikh.StockWatchdog.Entity.Alert;
import com.malikh.StockWatchdog.Repository.AlertRepository;

@Service
public class AlertService {
    @Autowired
    private AlertRepository alertRepo;
// Create
    public void createAlert(Alert a){
        alertRepo.save(a);
    }
// Read
    public List<Alert> getAllAlerts(AlertRepository a){
        return a.findAll();
    }
// Update
    public void updateAlert(){        
    }
// Delete
    public void deleteAlert(Long id){
        alertRepo.deleteById(id);
    }
}
