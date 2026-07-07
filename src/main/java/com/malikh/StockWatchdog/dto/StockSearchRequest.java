package com.malikh.stockwatchdog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockSearchRequest {
    @NotBlank
    @Size(max = 20, message = "Search query is too long")
    @Pattern(regexp = "^[a-zA-Z0-9 .&-]*$", message = "Search query contains invalid characters")
    private String query;
}