package com.CM.CookingMenu.dish.managers;

import com.CM.CookingMenu.dish.entities.Dish;
import com.CM.CookingMenu.dish.entities.DishDTO;
import com.CM.CookingMenu.ingredient.managers.IngredientManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class DishManager {
    @Autowired
    IngredientManager ingredientManager;
    public DishDTO toDto(Dish dish){
        DishDTO dto = new DishDTO();
        dto.setName(dish.getName());
        dto.setIngredientDTOS(ingredientManager.toDtoList(dish.getIngredients()));
        return dto;
    }
    public List<DishDTO> toDtoList(List<Dish> dishes){
        return dishes.stream()
                    .map(this::toDto)
                    .toList();
    }
}
