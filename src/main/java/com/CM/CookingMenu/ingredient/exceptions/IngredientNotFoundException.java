package com.CM.CookingMenu.ingredient.exceptions;

public class IngredientNotFoundException extends RuntimeException{
    public IngredientNotFoundException(String ingredientName) {
        super("Ingredient with name " + ingredientName + " not found.");
    }
}
