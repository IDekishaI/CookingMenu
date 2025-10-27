package com.CM.CookingMenu.auth.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Data transfer object for register request")
public record RegisterRequest(
        @Schema(
                description = "Users username",
                example = "example123"
        )
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50)
        String username,

        @Schema(
                description = "Users email",
                example = "example123@gmail.com"
        )
        @NotBlank(message = "Email is required")
        @Email
        String email,

        @Schema(
                description = "Users password",
                example = "examplepassword123"
        )
        @NotBlank(message = "Password is required")
        @Size(min = 6)
        String password,

        @Schema(
                description = "Users first name",
                example = "John"
        )
        String firstName,

        @Schema(
                description = "Users last name",
                example = "Doe"
        )
        String lastName
) {
}
