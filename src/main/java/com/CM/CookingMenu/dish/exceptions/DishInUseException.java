package com.CM.CookingMenu.dish.exceptions;

public class DishInUseException extends RuntimeException{
    public DishInUseException(String dishName) {
        super("Cannot remove dish" + dishName + " it is being used in existing menus.");
    }
}
