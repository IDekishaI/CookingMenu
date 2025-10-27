package com.CM.CookingMenu.auth.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Data transfer object for login request")
public record LoginRequest(
        @Schema(
                description = "Users username or email",
                example = "example123"
        )
        @NotBlank(message = "Username or Email is required")
        String usernameOrEmail,
        @Schema(
                description = "Users password",
                example = "examplepassword123"
        )
        @NotBlank(message = "Password is required")
        String password
) {
}
