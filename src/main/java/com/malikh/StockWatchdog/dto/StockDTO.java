package com.malikh.stockwatchdog.dto;

import java.time.Instant;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class StockDTO{
    @Id
    private Long id;
    private String symbol;
    private String companyName;
    private Double price;
    private String region;
    private String type;
    private Instant lastUpdated;
    private Double previousPrice;

}