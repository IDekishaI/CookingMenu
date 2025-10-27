package com.CM.CookingMenu.foodmenu.ai.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Data transfer object AI response suggestion")
public record FoodMenuSuggestionResponseDTO(
        @Schema(
                description = "List of suggested daily menus"
        )
        List<DailyMenuSuggestion> dailyMenuSuggestionList,
        @Schema(
                description = "Weekly Nutritional Summary of the suggested weekly menu"
        )
        WeeklyNutritionSummary weeklyNutritionSummary,
        @Schema(
                description = "Estimate cost per person for the suggested weekly menu",
                example = "13.5"
        )
        Double costEstimatePerPerson,
        @Schema(
                description = "Notes about the suggested weekly menu",
                example = "Wagyu is meant to be cooked at x degrees"
        )
        String notes
) {
}
