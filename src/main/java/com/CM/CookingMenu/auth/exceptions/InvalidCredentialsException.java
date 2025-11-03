package com.CM.CookingMenu.auth.exceptions;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException() {
        super("Invalid Credentials.");
    }
}
