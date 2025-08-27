package com.CM.CookingMenu.auth.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Username or Email is required")
    private String usernameOrEmail;
    @NotBlank(message = "Password is required")
    private String password;
}
