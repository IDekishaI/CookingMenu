package com.CM.CookingMenu.foodmenu.ai.dtos;

import java.util.List;

public record DailyMenuSuggestion (
    String date,
    List<String> suggestedDishes,
    String theme,
    Boolean isFastingFriendly
){}
