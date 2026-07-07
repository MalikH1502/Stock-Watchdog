package com.malikh.stockwatchdog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AlphaVantageMatch {

    @JsonProperty("1. symbol")
    private String symbol;

    @JsonProperty("2. name")
    private String name;

    @JsonProperty("3. type")
    private String type;

    @JsonProperty("4. region")
    private String region;

}
