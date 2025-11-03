package com.CM.CookingMenu.ingredient.exceptions;

public class IngredientInUseException extends RuntimeException{
    public IngredientInUseException(String ingredientName){
        super("Cannot remove ingredient " + ingredientName + " it is being used in existing dishes.");
    }
}
