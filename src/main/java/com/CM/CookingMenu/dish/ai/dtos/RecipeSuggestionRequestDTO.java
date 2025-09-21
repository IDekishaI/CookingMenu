package com.CM.CookingMenu.dish.ai.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSuggestionRequestDTO {
    @NotNull
    @NotEmpty(message = "Must provide at least one ingredient")
    private List<String> availableIngredients;
    private String mealType;
    private String targetGroup;
    private Boolean fastingFriendly;
}
