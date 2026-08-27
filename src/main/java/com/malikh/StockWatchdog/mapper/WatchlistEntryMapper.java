package com.malikh.stockwatchdog.mapper;

import org.springframework.stereotype.Component;

import com.malikh.stockwatchdog.dto.WatchlistEntryDTO;
import com.malikh.stockwatchdog.entity.WatchlistEntry;

@Component
public class WatchlistEntryMapper {
    private final StockMapper stockMapper;
    private final UserMapper userMapper;

    public WatchlistEntryMapper(StockMapper stockMapper, UserMapper userMapper) {
        this.stockMapper = stockMapper;
        this.userMapper = userMapper;
    }

    public WatchlistEntryDTO toDTO(WatchlistEntry entry) {
        WatchlistEntryDTO dto = new WatchlistEntryDTO();
        dto.setId(entry.getId());
        dto.setStock(stockMapper.toDTO(entry.getStock()));
        dto.setUser(userMapper.toDTO(entry.getUser()));
        dto.setDateAdded(entry.getDateAdded());
        return dto;
    }
}