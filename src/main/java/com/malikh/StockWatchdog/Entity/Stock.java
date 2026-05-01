package com.malikh.stockwatchdog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Data @NoArgsConstructor
public class Stock {
    @Id
    private String symbol;
    private String companyName;
    private Boolean isUpdated;
    private Double price;

}
