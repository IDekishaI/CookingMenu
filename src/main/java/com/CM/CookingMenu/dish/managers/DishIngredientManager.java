package com.CM.CookingMenu.dish.managers;

import com.CM.CookingMenu.dish.entities.Dish;
import com.CM.CookingMenu.dish.entities.DishIngredient;
import com.CM.CookingMenu.dish.entities.DishIngredientDTO;
import com.CM.CookingMenu.dish.repositories.DishRepository;
import com.CM.CookingMenu.ingredient.entities.Ingredient;
import com.CM.CookingMenu.ingredient.repositories.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DishIngredientManager {
    private final DishRepository dishRepo;
    private final IngredientRepository ingredientRepo;
    public DishIngredientDTO toDto(DishIngredient dishIngredient){
        DishIngredientDTO dto = new DishIngredientDTO();
        dto.setIngredientName(dishIngredient.getIngredient().getName());
        dto.setQuantity(dishIngredient.getQuantity());
        return dto;
    }
    public List<DishIngredientDTO> toDtoList(List<DishIngredient> dishIngredients){
        return dishIngredients.stream()
                            .map(this::toDto)
                            .toList();
    }
    public DishIngredient toEntity(DishIngredientDTO dto, Dish dish){
        DishIngredient dishIngredient = new DishIngredient();
        Ingredient ingredient = ingredientRepo.findByName(dto.getIngredientName()).orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingredient not found."));
        dishIngredient.setDish(dish);
        dishIngredient.setIngredient(ingredient);
        dishIngredient.setQuantity(dto.getQuantity());
        return dishIngredient;
    }
    public List<DishIngredient> toEntityList(List<DishIngredientDTO> dtos, Dish dish){
        return dtos.stream()
                .map(dto -> toEntity(dto, dish))
                .toList();
    }
}
