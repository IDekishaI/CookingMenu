package com.CM.CookingMenu.foodmenu.ai.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyMenuSuggestion {
    private String date;
    private List<String> suggestedDishes;
    private String theme;
    private Boolean isFastingFriendly;
}
