package com.CM.CookingMenu.auth.dtos;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data transfer object for authentication response")
public record AuthResponse(
        @Schema(
                description = "JWT Token",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
        )
        String token,
        @Schema(
                description = "Users username",
                example = "example123"
        )
        String username,
        @Schema(
                description = "Users role",
                example = "COOK"
        )
        String role,
        @Schema(
                description = "Expires in 1 hour",
                example = "3600000"
        )
        Long expiresIn
) {
}
