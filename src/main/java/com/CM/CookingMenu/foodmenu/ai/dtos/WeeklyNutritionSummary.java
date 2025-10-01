package com.CM.CookingMenu.foodmenu.ai.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyNutritionSummary {
    private Double averageCaloriesPerDay;
    private Double averageProteinPerDay;
    private Double averageCarbsPerDay;
    private Double averageFatPerDay;
    private String balanceRating;
}
