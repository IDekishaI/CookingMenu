package com.CM.CookingMenu.auth.dtos;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String username;
    private String role;
    private Long expiresIn = 3600000L;
}
