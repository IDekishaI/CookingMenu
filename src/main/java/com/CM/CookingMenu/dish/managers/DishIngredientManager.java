package com.CM.CookingMenu.dish.managers;

import com.CM.CookingMenu.dish.entities.Dish;
import com.CM.CookingMenu.dish.entities.DishIngredient;
import com.CM.CookingMenu.dish.dtos.DishIngredientDTO;
import com.CM.CookingMenu.ingredient.entities.Ingredient;
import com.CM.CookingMenu.ingredient.repositories.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DishIngredientManager {
    private final IngredientRepository ingredientRepo;
    public DishIngredientDTO toDto(DishIngredient dishIngredient){
        if(dishIngredient == null)
            throw new IllegalArgumentException("DishIngredient cannot be null.");

        if(dishIngredient.getIngredient() == null)
            throw new IllegalArgumentException("Ingredient in DishIngredient cannot be null.");

        DishIngredientDTO dto = new DishIngredientDTO(dishIngredient.getIngredient().getName(), dishIngredient.getQuantity());
        return dto;
    }
    public List<DishIngredientDTO> toDtoList(List<DishIngredient> dishIngredients){
        if(dishIngredients == null)
            return new ArrayList<>();

        return dishIngredients.stream()
                            .filter(Objects::nonNull)
                            .map(this::toDto)
                            .toList();
    }
    public DishIngredient toEntity(DishIngredientDTO dto, Dish dish){
        if(dish == null)
            throw new IllegalArgumentException("Dish cannot be null.");

        DishIngredient dishIngredient = new DishIngredient();
        Ingredient ingredient = ingredientRepo.findByName(dto.ingredientName().trim()).orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingredient not found."));
        dishIngredient.setDish(dish);
        dishIngredient.setIngredient(ingredient);
        dishIngredient.setQuantity(dto.quantity());
        return dishIngredient;
    }
    public List<DishIngredient> toEntityList(List<DishIngredientDTO> dtos, Dish dish){
        if(dish == null)
            throw new IllegalArgumentException("Dish cannot be null.");

        return dtos.stream()
                .filter(Objects::nonNull)
                .map(dto -> toEntity(dto, dish))
                .toList();
    }
}
