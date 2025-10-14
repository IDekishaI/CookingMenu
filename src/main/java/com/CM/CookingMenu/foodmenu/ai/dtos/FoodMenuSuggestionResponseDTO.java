package com.CM.CookingMenu.foodmenu.ai.dtos;

import java.util.List;

public record FoodMenuSuggestionResponseDTO (
    List<DailyMenuSuggestion> dailyMenuSuggestionList,
    WeeklyNutritionSummary weeklyNutritionSummary,
    Double costEstimatePerPerson,
    String notes
){}
