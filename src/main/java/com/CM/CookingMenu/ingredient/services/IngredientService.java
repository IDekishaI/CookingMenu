package com.CM.CookingMenu.ingredient.services;

import com.CM.CookingMenu.dish.repositories.DishIngredientRepository;
import com.CM.CookingMenu.ingredient.dtos.IngredientDTO;
import com.CM.CookingMenu.ingredient.entities.Ingredient;
import com.CM.CookingMenu.ingredient.exceptions.IngredientAlreadyExistsException;
import com.CM.CookingMenu.ingredient.exceptions.IngredientInUseException;
import com.CM.CookingMenu.ingredient.exceptions.IngredientNotFoundException;
import com.CM.CookingMenu.ingredient.managers.IngredientManager;
import com.CM.CookingMenu.ingredient.repositories.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IngredientService {
    private final IngredientRepository ingredientRepo;
    private final IngredientManager ingredientManager;
    private final DishIngredientRepository dishIngredientRepo;

    public List<IngredientDTO> getAllIngredients() {
        return ingredientManager.toDtoList(ingredientRepo.findAll());
    }

    @Transactional
    public void addIngredient(IngredientDTO dto) {
        if (ingredientRepo.findByName(dto.name().trim()).isPresent()) {
            throw new IngredientAlreadyExistsException(dto.name().trim());
        }
        Ingredient ingredient = ingredientManager.toEntity(dto);
        ingredientRepo.save(ingredient);
    }

    @Transactional
    public void deleteIngredientByName(String name) {
        Ingredient ingredient = ingredientRepo.findByName(name.trim()).orElseThrow(() -> new IngredientNotFoundException(name.trim()));

        if (dishIngredientRepo.existsByIngredientName(name.trim()))
            throw new IngredientInUseException(name.trim());

        ingredientRepo.delete(ingredient);
    }

    @Transactional
    public void updateIngredient(IngredientDTO ingredientDTO) {
        String ingredientName = ingredientDTO.name().trim();

        Ingredient ingredient = ingredientRepo.findByName(ingredientName).orElseThrow(() -> new IngredientNotFoundException(ingredientName));

        ingredient.setFastingSuitable(ingredientDTO.fastingSuitable());

        ingredientRepo.save(ingredient);
    }

    public Ingredient findIngredientByName(String ingredientName){
        return ingredientRepo.findByName(ingredientName.trim()).orElseThrow(() -> new IngredientNotFoundException(ingredientName));
    }

    public List<Ingredient> findIngredientsByNames(List<String> ingredientNames){
        List<Ingredient> ingredients = new ArrayList<>();
        for(String name : ingredientNames){
            Ingredient ingredient = findIngredientByName(name);
            ingredients.add(ingredient);
        }
        return ingredients;
    }
}
