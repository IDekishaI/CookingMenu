package com.CM.CookingMenu.handlers;

import com.CM.CookingMenu.auth.exceptions.EmailAlreadyExistsException;
import com.CM.CookingMenu.auth.exceptions.InvalidCredentialsException;
import com.CM.CookingMenu.auth.exceptions.UsernameAlreadyExistsException;
import com.CM.CookingMenu.dish.exceptions.DishAlreadyExistsException;
import com.CM.CookingMenu.dish.exceptions.DishInUseException;
import com.CM.CookingMenu.dish.exceptions.DishNotFoundException;
import com.CM.CookingMenu.foodmenu.exceptions.FoodMenuAlreadyExistsException;
import com.CM.CookingMenu.foodmenu.exceptions.FoodMenuNotFoundException;
import com.CM.CookingMenu.ingredient.exceptions.IngredientAlreadyExistsException;
import com.CM.CookingMenu.ingredient.exceptions.IngredientInUseException;
import com.CM.CookingMenu.ingredient.exceptions.IngredientNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        System.out.println("Validation error occurred: {}" + ex.getMessage());

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Validation failed");
        response.put("errors", fieldErrors);
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        System.out.println("Validation error occurred: " + ex.getMessage());

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            fieldErrors.put(field, message);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Validation failed");
        response.put("errors", fieldErrors);
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(
            ResponseStatusException ex) {

        System.out.println("Business logic error: {}" + ex.getReason());

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", ex.getReason());
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {

        System.out.println("Database constraint violation: {}" + ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);

        String message = ex.getMessage();
        if (message.contains("unique constraint") || message.contains("duplicate")) {
            response.put("message", "A record with this information already exists");
        } else if (message.contains("foreign key")) {
            response.put("message", "Cannot delete this record because it's being used elsewhere");
        } else {
            response.put("message", "Database operation failed");
        }

        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler({
            IngredientNotFoundException.class,
            DishNotFoundException.class,
            FoodMenuNotFoundException.class
    })
    public ResponseEntity<Map<String, Object>> handleNotFoundException(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler({
            IngredientAlreadyExistsException.class,
            DishAlreadyExistsException.class,
            FoodMenuAlreadyExistsException.class,
            UsernameAlreadyExistsException.class,
            EmailAlreadyExistsException.class
    })
    public ResponseEntity<Map<String, Object>> handleAlreadyExistsException(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({
            IngredientInUseException.class,
            DishInUseException.class
    })
    public ResponseEntity<Map<String, Object>> handleConflictException(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler({
            InvalidCredentialsException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(Exception ex){
        System.out.println("Authentication error: " + ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", ex instanceof InvalidCredentialsException
                ? ex.getMessage()
                : "Invalid username or password");
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {

        System.out.println("Unexpected error occurred" + ex);

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "An unexpected error occurred. Please try again later.");
        response.put("timestamp", LocalDateTime.now());

        // Don't expose internal error details to users
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
