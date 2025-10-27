package com.CM.CookingMenu.dish.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Data transfer object for Dish Ingredient")
public record DishIngredientDTO(
        @Schema(
                description = "Name of ingredient",
                example = "Tomato",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Ingredient name cannot be null.")
        @NotBlank(message = "Ingredient name cannot be blank.")
        String ingredientName,
        @Schema(
                description = "Quantity of the ingredient",
                example = "100",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Quantity is required.")
        @Positive(message = "Quantity must be positive.")
        Double quantity
) {
}
