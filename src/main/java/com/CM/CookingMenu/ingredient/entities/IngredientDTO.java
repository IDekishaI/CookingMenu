package com.CM.CookingMenu.ingredient.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientDTO {
    @NotBlank(message = "Ingredient name cannot be blank.")
    @Size(min = 2, max = 100)
    private String name;

    @NotNull(message = "fastingSuitable field is required.")
    private boolean fastingSuitable;
}
