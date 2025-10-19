package com.CM.CookingMenu.foodmenu.ai.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
@Schema(description = "AI Suggested Menu for the day")
public record DailyMenuSuggestion (
    @Schema(
            description = "Date for the suggested menu",
            example = "2025-10-20"
    )
    String date,

    @Schema(
            description = "List of suggested dishes for the menu"
    )
    List<String> suggestedDishes,

    @Schema(
            description = "Theme of the suggested menu",
            example = "Full of protein monday"
    )
    String theme,

    @Schema(
            description = "Fasting suitability of the menu",
            example = "false"
    )
    Boolean isFastingFriendly
){}
