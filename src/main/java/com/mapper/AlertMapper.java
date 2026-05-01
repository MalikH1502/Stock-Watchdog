package com.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.malikh.stockwatchdog.dto.AlertDTO;
import com.malikh.stockwatchdog.entity.Alert;

@Component
public class AlertMapper {
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private UserMapper userMapper;

    public AlertDTO toDTO(Alert alert){
        AlertDTO dto = new AlertDTO();
        dto.setId(alert.getId());
        dto.setCondition(alert.getCondition());
        dto.setValue(alert.getValue());
        dto.setIsTrue(alert.getIsTrue());
        dto.setStockDTO(stockMapper.toDTO(alert.getStock()));
        dto.setUser(userMapper.toDTO(alert.getUser()));
        return dto;
    }
}
