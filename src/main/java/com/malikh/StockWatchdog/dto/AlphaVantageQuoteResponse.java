package com.malikh.stockwatchdog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlphaVantageQuoteResponse {

    @JsonProperty("Global Quote")
    private GlobalQuote globalQuote;

    @Data
    @NoArgsConstructor
    public static class GlobalQuote {
        @JsonProperty("05. price")
        private String price;
    }
}