package com.CM.CookingMenu.dish.exceptions;

public class DishAlreadyExistsException extends RuntimeException{
    public DishAlreadyExistsException(String dishName) {
        super("Dish with the name " + dishName + " already exists.");
    }
}
