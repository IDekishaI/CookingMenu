package com.CM.CookingMenu.foodmenu.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodMenuDishDTO {
    @NotNull(message = "Dish name cannot be null.")
    @NotBlank(message = "Dish name cannot be blank")
    String dishName;
}
