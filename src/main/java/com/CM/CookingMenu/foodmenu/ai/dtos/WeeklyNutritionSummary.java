package com.CM.CookingMenu.foodmenu.ai.dtos;

public record WeeklyNutritionSummary (
    Double averageCaloriesPerDay,
    Double averageCarbsPerDay,
    Double averageFatPerDay,
    Double averageProteinPerDay,
    String balanceRating
){}
