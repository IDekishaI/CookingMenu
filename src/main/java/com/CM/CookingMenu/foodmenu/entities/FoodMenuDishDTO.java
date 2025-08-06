package com.CM.CookingMenu.foodmenu.entities;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodMenuDishDTO {
    @NotBlank(message = "Dish name cannot be blank")
    String dishName;
}
