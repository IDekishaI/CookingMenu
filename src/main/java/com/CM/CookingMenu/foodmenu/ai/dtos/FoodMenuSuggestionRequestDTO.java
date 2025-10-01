package com.CM.CookingMenu.foodmenu.ai.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodMenuSuggestionRequestDTO {
    @NotNull(message = "Start date is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format. Use YYYY-MM-DD")
    private String startDate;
    private List<String> preferredIngredients;
    private List<String> avoidIngredients;
    private String budgetContstraint;
    private String nutritionalFocus;
    private Boolean fastingFriendlyRequired;
}
