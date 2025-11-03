package com.CM.CookingMenu.ingredient.exceptions;

public class IngredientAlreadyExistsException extends RuntimeException{
    public IngredientAlreadyExistsException(String ingredientName){
        super("Ingredient with name " + ingredientName + " already exists.");
    }
}
