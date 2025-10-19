package com.CM.CookingMenu.foodmenu.ai.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

@Schema(description = "Data transfer object for Food Menu Suggestion Request")
public record FoodMenuSuggestionRequestDTO (
    @Schema(
            description = "Start date of the food menus",
            example = "2025-10-20",
            requiredMode = Schema.RequiredMode.REQUIRED,
            pattern = "YYYY-MM-DD"
    )
    @NotNull(message = "Start date is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format. Use YYYY-MM-DD")
    String startDate,

    @Schema(
            description = "Preffered ingredients for the food menus"
    )
    List<String> preferredIngredients,

    @Schema(
            description = "List of ingredients to avoid for the food menus"
    )
    List<String> avoidIngredients,

    @Schema(
            description = "Budget constraint for the food menus",
            example = "High cost"
    )
    String budgetContstraint,

    @Schema(
            description = "Nutritional focus for the food menus",
            example = "High protein"
    )
    String nutritionalFocus,

    @Schema(
            description = "Fasting suitability for the food menus",
            example = "false"
    )
    Boolean fastingFriendlyRequired
){}
