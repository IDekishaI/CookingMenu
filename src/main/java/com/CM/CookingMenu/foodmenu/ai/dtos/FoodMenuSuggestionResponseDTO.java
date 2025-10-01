package com.CM.CookingMenu.foodmenu.ai.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FoodMenuSuggestionResponseDTO {
    private List<DailyMenuSuggestion> dailyMenuSuggestionList;
    private WeeklyNutritionSummary weeklyNutritionSummary;
    private Double costEstimatePerPerson;
    private String notes;
}
