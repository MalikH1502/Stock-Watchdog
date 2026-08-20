package com.malikh.stockwatchdog.mapper;

import org.springframework.stereotype.Component;

import com.malikh.stockwatchdog.dto.StockDTO;
import com.malikh.stockwatchdog.entity.Stock;
@Component
public class StockMapper {
   // StockMapper.java
public StockDTO toDTO(Stock stock) {
    StockDTO dto = new StockDTO();
    dto.setId(stock.getId());
    dto.setSymbol(stock.getSymbol());
    dto.setPrice(stock.getPrice());
    dto.setPreviousPrice(stock.getPreviousPrice());
    dto.setCompanyName(stock.getCompanyName());
    dto.setType(stock.getType());
    dto.setRegion(stock.getRegion());
    dto.setLastUpdated(stock.getLastUpdated());
    return dto;
}
}
