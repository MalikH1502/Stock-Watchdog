package com.malikh.stockwatchdog.dto;
import lombok.Data;
import jakarta.validation.constraints.Email;
@Data
public class UserRegistrationRequest {
    private String username;
    @Email
    private String email;
    
    private String password;
}
