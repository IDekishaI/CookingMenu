package com.CM.CookingMenu.dish.ai.dtos;

import java.util.List;

public record RecipeSuggestionResponseDTO (
    String dishName,
    List<String> requiredIngredients,
    String cookingInstructions,
    Boolean isFastingFriendly
){}
