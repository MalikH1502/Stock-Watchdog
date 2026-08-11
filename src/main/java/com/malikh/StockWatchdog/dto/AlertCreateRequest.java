package com.malikh.stockwatchdog.dto;

import com.malikh.stockwatchdog.entity.Alert;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AlertCreateRequest {
    @NotNull
    private Long stockId;

    @NotNull
    private Alert.Condition condition;

    @NotNull
    @Positive
    private Double value;
}