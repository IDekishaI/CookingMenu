package com.CM.CookingMenu.ingredient.controllers;

import com.CM.CookingMenu.ingredient.entities.IngredientDTO;
import com.CM.CookingMenu.ingredient.services.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/ingredients")
public class IngredientController {
    private final IngredientService ingredientService;
    @GetMapping
    public ResponseEntity<List<IngredientDTO>> getAllIngredients(){
        return ResponseEntity.ok(ingredientService.getAllIngredients());
    }
    @PostMapping
    public ResponseEntity<String> addIngredient(@RequestBody IngredientDTO ingredientDTO){
        ingredientService.addIngredient(ingredientDTO);
        return ResponseEntity.ok("Ingredient Added.");
    }
}
