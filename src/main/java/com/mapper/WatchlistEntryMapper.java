package com.mapper;

import com.malikh.stockwatchdog.dto.WatchlistEntryDTO;
import com.malikh.stockwatchdog.entity.WatchlistEntry;

public class WatchlistEntryMapper {
    private final StockMapper stockMapper = new StockMapper();

    public WatchlistEntryDTO toDTO(WatchlistEntry entry) {
        WatchlistEntryDTO dto = new WatchlistEntryDTO();
        dto.setId(entry.getId());
        dto.setStock(stockMapper.toDTO(entry.getStock()));
        return dto;
    }
}
