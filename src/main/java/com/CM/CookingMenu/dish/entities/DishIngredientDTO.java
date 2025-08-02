package com.CM.CookingMenu.dish.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DishIngredientDTO {
    String ingredientName;
    Boolean fastingSuitable;
    Double quantity;
}
