package com.malikh.stockwatchdog.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient.Builder;

import com.malikh.stockwatchdog.dto.StockSearchRequest;
import com.malikh.stockwatchdog.entity.Stock;
import com.malikh.stockwatchdog.dto.AlphaVantageMatch;
import com.malikh.stockwatchdog.dto.AlphaVantageSearchResponse;

@Service
public class AlphaVantageService {
    private final RestClient.Builder restClientBuilder;
    private final RestClient restClient;

    @Value("${alphavantage.api.key}")
    private String apiKey;

    public AlphaVantageService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("https://www.alphavantage.co").build();
    }

    public List<AlphaVantageMatch> searchSymbol(String symbol) {
        AlphaVantageSearchResponse alphaVantageSearchResponse = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/query")
                        .queryParam("function", "SYMBOL_SEARCH")
                        .queryParam("keywords", symbol)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .body(AlphaVantageSearchResponse.class);
        if (alphaVantageSearchResponse == null || alphaVantageSearchResponse.getMatches() == null) {

            return new ArrayList<>();
        }
        return alphaVantageSearchResponse.getMatches();
    }

}
