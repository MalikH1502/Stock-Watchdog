package com.malikh.stockwatchdog.dto;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class StockDTO{
    @Id
    private Long id;
    private String symbol;
    private String companyName;
    private Double price;

}