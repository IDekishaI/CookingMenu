package com.CM.CookingMenu.foodmenu.exceptions;

public class FoodMenuNotFoundException extends RuntimeException{
    public FoodMenuNotFoundException(String foodMenuDate) {
        super("FoodMenu on the date " + foodMenuDate + " not found.");
    }
}
