package com.CM.CookingMenu.ingredient.services;

import com.CM.CookingMenu.dish.repositories.DishIngredientRepository;
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
    private final DishIngredientRepository dishIngredientRepo;
    public List<IngredientDTO> getAllIngredients(){
        return ingredientManager.toDtoList(ingredientRepo.findAll());
    }
    @Transactional
    public void addIngredient(IngredientDTO dto){
        if(ingredientRepo.findByName(dto.name().trim()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingredient name already exists.");
        }
        Ingredient ingredient = ingredientManager.toEntity(dto);
        ingredientRepo.save(ingredient);
    }
    @Transactional
    public void deleteIngredientByName(String name){
        Ingredient ingredient = ingredientRepo.findByName(name.trim()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient not found."));

        if(dishIngredientRepo.existsByIngredientName(name.trim()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot remove ingredient. It is being used in existing dishes.");

        ingredientRepo.delete(ingredient);
    }
    @Transactional
    public void updateIngredient(IngredientDTO ingredientDTO){
        String ingredientName = ingredientDTO.name().trim();

        Ingredient ingredient = ingredientRepo.findByName(ingredientName).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient with name " + ingredientName + " not found"));

        ingredient.setFastingSuitable(ingredientDTO.fastingSuitable());

        ingredientRepo.save(ingredient);
    }
}
