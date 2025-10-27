package com.CM.CookingMenu.dish.ai.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Data transfer object for AI recipe suggestion request")
public record RecipeSuggestionRequestDTO(
        @Schema(
                description = "List of available ingredients",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        @NotEmpty(message = "Must provide at least one ingredient")
        List<String> availableIngredients,
        @Schema(
                description = "Type of the meal",
                example = "Cafeteria lunch"
        )
        String mealType,
        @Schema(
                description = "Target group",
                example = "Partner executives"
        )
        String targetGroup,
        @Schema(
                description = "Preferred fasting suitability of the recipe",
                example = "false"
        )
        Boolean fastingFriendly
) {
}
