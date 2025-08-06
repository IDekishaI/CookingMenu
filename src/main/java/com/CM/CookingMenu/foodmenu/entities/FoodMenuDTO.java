package com.CM.CookingMenu.foodmenu.entities;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodMenuDTO {
    @NotNull
    private LocalDate date;
    @NotEmpty
    @Valid
    List<FoodMenuDishDTO> foodMenuDishDTOS;
}
