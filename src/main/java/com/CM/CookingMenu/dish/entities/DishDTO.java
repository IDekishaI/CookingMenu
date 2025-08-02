package com.CM.CookingMenu.dish.entities;

import com.CM.CookingMenu.ingredient.entities.IngredientDTO;
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
    String name;
    List<IngredientDTO> ingredientDTOS;
}
