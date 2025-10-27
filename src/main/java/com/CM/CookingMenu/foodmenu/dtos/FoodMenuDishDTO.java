package com.CM.CookingMenu.foodmenu.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Data transfer object for food menu dish")
public record FoodMenuDishDTO(
        @Schema(
                description = "Name of dish",
                example = "French Fries",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Dish name cannot be null.")
        @NotBlank(message = "Dish name cannot be blank")
        String dishName
) {
}
