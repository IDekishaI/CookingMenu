package com.CM.CookingMenu.ingredient.controllers;

import com.CM.CookingMenu.ingredient.dtos.IngredientDTO;
import com.CM.CookingMenu.ingredient.services.IngredientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/ingredients")
@Validated
public class IngredientController {
    private final IngredientService ingredientService;
    @GetMapping
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<List<IngredientDTO>> getAllIngredients(){
        return ResponseEntity.ok(ingredientService.getAllIngredients());
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<String> addIngredient(@Valid @RequestBody IngredientDTO ingredientDTO){
        ingredientService.addIngredient(ingredientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Ingredient Added.");
    }
    @DeleteMapping("/delete/{name}")
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<String> deleteIngredient(@PathVariable
                                                       @Pattern(regexp = "^[a-zA-Z_\\s]{2,100}$", message = "Invalid ingredient name format")
                                                       @NotBlank(message = "Ingredient name cannot be blank.")String name){
        if(name == null || name.trim().isBlank())
            throw new IllegalArgumentException("Ingredient name cannot be null or blank");
        ingredientService.deleteIngredientByName(name);
        return ResponseEntity.ok("Ingredient deleted successfully");
    }
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<String> updateIngredient(@Valid @RequestBody IngredientDTO ingredientDTO){
        ingredientService.updateIngredient(ingredientDTO);
        return ResponseEntity.ok("Ingredient updated successfully.");
    }
}
