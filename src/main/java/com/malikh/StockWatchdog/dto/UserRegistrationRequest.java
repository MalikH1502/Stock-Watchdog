package com.malikh.stockwatchdog.dto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.annotation.JsonPOJOBuilder;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    @NotBlank
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters") 
    private String username;
    @NotBlank(message = "Email address required")
    @Email(message = "Email must be valid")
    private String email;
    @NotBlank
    @Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters")
    private String password;
}
