package com.CM.CookingMenu.foodmenu.exceptions;

public class FoodMenuAlreadyExistsException extends RuntimeException{
    public FoodMenuAlreadyExistsException(String foodMenuDate) {
        super("FoodMenu on the date " + foodMenuDate + " already exists.");
    }
}
