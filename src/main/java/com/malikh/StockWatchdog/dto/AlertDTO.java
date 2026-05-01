package com.malikh.stockwatchdog.dto;

import com.malikh.stockwatchdog.entity.Alert;

import lombok.Data;
@Data
public class AlertDTO {
    private Long id;
    private Alert.Condition condition;
    private Double value;
    private Boolean isTrue;
    private StockDTO stock;
    private UserDTO user;
}
