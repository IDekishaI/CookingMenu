package com.CM.CookingMenu.dish.ai.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSuggestionResponseDTO {
    private String dishName;
    private List<String> requiredIngredients;
    private String cookingInstructions;
    private Boolean isFastingFriendly;
}
