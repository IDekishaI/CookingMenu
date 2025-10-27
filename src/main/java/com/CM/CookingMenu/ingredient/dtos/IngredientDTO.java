package com.CM.CookingMenu.ingredient.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Data transfer object for ingredient information")
public record IngredientDTO(
        @Schema(
                description = "Name of ingredient",
                example = "Tomato",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Ingredient name cannot be null.")
        @NotBlank(message = "Ingredient name cannot be blank.")
        @Size(min = 2, max = 100)
        String name,

        @Schema(
                description = "Whether this ingredient is suitable for fasting",
                example = "false",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "fastingSuitable field is required.")
        Boolean fastingSuitable
) {
}
