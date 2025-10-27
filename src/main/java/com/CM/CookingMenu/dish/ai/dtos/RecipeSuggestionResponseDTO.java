package com.CM.CookingMenu.dish.ai.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Data transfer object for AI recipe suggestion response")
public record RecipeSuggestionResponseDTO(
        @Schema(
                description = "Name of the dish",
                example = "Seared wagyu in garlic"
        )
        String dishName,
        @Schema(
                description = "List of required ingredients for the dish"
        )
        List<String> requiredIngredients,
        @Schema(
                description = "Instructions for cooking the meal",
                example = "Cook the wagyu for 5 minutes on each side"
        )
        String cookingInstructions,
        @Schema(
                description = "Fasting suitability of the dish",
                example = "true"
        )
        Boolean isFastingFriendly
) {
}
