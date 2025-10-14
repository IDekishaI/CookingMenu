package com.CM.CookingMenu.dish.ai.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RecipeSuggestionRequestDTO (
    @NotNull
    @NotEmpty(message = "Must provide at least one ingredient")
    List<String> availableIngredients,
    String mealType,
    String targetGroup,
    Boolean fastingFriendly
){}
