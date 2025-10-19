package com.CM.CookingMenu.foodmenu.ai.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
@Schema(description = "Data transfer object AI response suggestion")
public record FoodMenuSuggestionResponseDTO (
    @Schema(
            description = "List of daily menus"
    )
    List<DailyMenuSuggestion> dailyMenuSuggestionList,
    WeeklyNutritionSummary weeklyNutritionSummary,
    Double costEstimatePerPerson,
    String notes
){}
