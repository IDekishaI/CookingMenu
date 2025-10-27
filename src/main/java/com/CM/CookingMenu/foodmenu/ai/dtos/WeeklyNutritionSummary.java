package com.CM.CookingMenu.foodmenu.ai.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Weekly nutritional summary for suggested weekly menu")
public record WeeklyNutritionSummary(
        @Schema(
                description = "Average calories per day",
                example = "2350"
        )
        Double averageCaloriesPerDay,
        @Schema(
                description = "Average grams of carbs per day",
                example = "50"
        )
        Double averageCarbsPerDay,
        @Schema(
                description = "Average grams of fat per day",
                example = "50"
        )
        Double averageFatPerDay,
        @Schema(
                description = "Average grams of protein per day",
                example = "50"
        )
        Double averageProteinPerDay,
        @Schema(
                description = "Balance rating for the nutrients",
                example = "Perfectly balanced"
        )
        String balanceRating
) {
}
