package com.CM.CookingMenu.ingredient.services;

import com.CM.CookingMenu.ingredient.managers.IngredientManager;
import com.CM.CookingMenu.ingredient.repositories.IngredientRepository;
import com.CM.CookingMenu.ingredient.entities.Ingredient;
import com.CM.CookingMenu.ingredient.entities.IngredientDTO;
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
