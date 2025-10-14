package com.CM.CookingMenu.foodmenu.ai.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record FoodMenuSuggestionRequestDTO (
    @NotNull(message = "Start date is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format. Use YYYY-MM-DD")
    String startDate,
    List<String> preferredIngredients,
    List<String> avoidIngredients,
    String budgetContstraint,
    String nutritionalFocus,
    Boolean fastingFriendlyRequired
){}
