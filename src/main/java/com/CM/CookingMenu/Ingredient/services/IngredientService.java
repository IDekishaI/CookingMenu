package com.CM.CookingMenu.Ingredient.services;

import com.CM.CookingMenu.Ingredient.managers.IngredientManager;
import com.CM.CookingMenu.Ingredient.repositories.IngredientRepository;
import com.CM.CookingMenu.Ingredient.entities.Ingredient;
import com.CM.CookingMenu.Ingredient.entities.IngredientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepo;
    private final IngredientManager ingredientManager;
    public List<IngredientDTO> getAllIngredients(){
        return ingredientManager.toDtoList(ingredientRepo.findAll());
    }
    public void addIngredient(IngredientDTO dto){
        Ingredient ingredient = ingredientManager.toEntity(dto);
        ingredientRepo.save(ingredient);
    }
}
