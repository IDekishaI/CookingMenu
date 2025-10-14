package com.CM.CookingMenu.foodmenu.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FoodMenuDishDTO (
    @NotNull(message = "Dish name cannot be null.")
    @NotBlank(message = "Dish name cannot be blank")
    String dishName
){}
