package com.CM.CookingMenu.dish.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DishIngredientDTO {
    @NotNull(message = "Ingredient name cannot be null.")
    @NotBlank(message = "Ingredient name cannot be blank.")
    private String ingredientName;
    @NotNull(message = "Quantity is required.")
    @Positive(message = "Quantity must be positive.")
    private Double quantity;
}
