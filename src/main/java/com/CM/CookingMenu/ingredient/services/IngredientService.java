package com.CM.CookingMenu.ingredient.services;

import com.CM.CookingMenu.ingredient.managers.IngredientManager;
import com.CM.CookingMenu.ingredient.repositories.IngredientRepository;
import com.CM.CookingMenu.ingredient.entities.Ingredient;
import com.CM.CookingMenu.ingredient.dtos.IngredientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IngredientService {
    private final IngredientRepository ingredientRepo;
    private final IngredientManager ingredientManager;
    public List<IngredientDTO> getAllIngredients(){
        return ingredientManager.toDtoList(ingredientRepo.findAll());
    }
    @Transactional
    public void addIngredient(IngredientDTO dto){
        if(ingredientRepo.findByName(dto.getName().trim()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingredient name already exists.");
        }
        Ingredient ingredient = ingredientManager.toEntity(dto);
        ingredientRepo.save(ingredient);
    }
}
