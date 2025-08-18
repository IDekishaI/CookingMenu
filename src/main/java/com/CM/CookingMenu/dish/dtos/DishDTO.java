package com.CM.CookingMenu.dish.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Dish name cannot be null.")
    @NotBlank(message = "Dish name cannot be blank.")
    @Size(min = 2, max = 100)
    String name;
    @NotNull(message = "Dish Ingredients cannot be null.")
    @NotEmpty(message = "Must have at least 1 ingredient.")
    @Valid
    List<DishIngredientDTO> dishIngredientDTOS;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean fastingSuitable;
}
