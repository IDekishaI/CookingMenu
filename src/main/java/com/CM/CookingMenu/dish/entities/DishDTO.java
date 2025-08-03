package com.CM.CookingMenu.dish.entities;

import com.CM.CookingMenu.ingredient.entities.IngredientDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DishDTO {
    @NotBlank(message = "Dish name cannot be blank.")
    @Size(min = 2, max = 100)
    String name;
    @NotEmpty(message = "Must have at least 1 ingredient.")
    @Valid
    List<DishIngredientDTO> dishIngredientDTOS;
}
