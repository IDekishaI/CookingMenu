package com.CM.CookingMenu.dish.exceptions;

public class DishNotFoundException extends RuntimeException{
    public DishNotFoundException(String dishName) {
        super("Dish with the name " + dishName + " not found.");
    }
}
