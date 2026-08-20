package com.malikh.stockwatchdog.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WatchlistAddRequest {
    @NotNull
    private Long stockId;
}