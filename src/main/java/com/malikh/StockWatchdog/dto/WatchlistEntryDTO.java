package com.malikh.stockwatchdog.dto;

import java.time.LocalDate;

public class WatchlistEntryDTO {
    private Long id;
    private StockDTO stock;
    private UserDTO user;
    private LocalDate dateAdded;
}
