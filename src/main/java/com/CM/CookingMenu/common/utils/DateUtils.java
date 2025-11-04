package com.CM.CookingMenu.common.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateUtils {
    private DateUtils(){}
    public static LocalDate parseStringToLocalDate(String stringDate){
        try {
            return LocalDate.parse(stringDate);
        } catch (DateTimeParseException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format: " + stringDate);
        }
    }
}
