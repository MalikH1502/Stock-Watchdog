package com.malikh.stockwatchdog.dto;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class AlphaVantageSearchResponse {
    @JsonProperty("bestMatches")
    private List<AlphaVantageMatch> bestMatches;

    @JsonProperty("Note")
    private String note;

    @JsonProperty("Information")
    private String information;

    public List<AlphaVantageMatch> getMatches(){
        return bestMatches;
    }
}
