package com.CM.CookingMenu.ingredient.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record IngredientDTO (
    @NotNull(message = "Ingredient name cannot be null.")
    @NotBlank(message = "Ingredient name cannot be blank.")
    @Size(min = 2, max = 100)
    String name,

    @NotNull(message = "fastingSuitable field is required.")
    Boolean fastingSuitable
){}
