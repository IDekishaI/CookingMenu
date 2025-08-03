package com.CM.CookingMenu.dish.managers;

import com.CM.CookingMenu.dish.entities.Dish;
import com.CM.CookingMenu.dish.entities.DishDTO;
import com.CM.CookingMenu.dish.entities.DishIngredient;
import com.CM.CookingMenu.dish.entities.DishIngredientDTO;
import com.CM.CookingMenu.ingredient.managers.IngredientManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@RequiredArgsConstructor
public class DishManager {
    private final DishIngredientManager dishIngredientManager;
    public DishDTO toDto(Dish dish){
        DishDTO dto = new DishDTO();
        dto.setName(dish.getName());
        dto.setDishIngredientDTOS(dishIngredientManager.toDtoList(dish.getDishIngredients()));
        return dto;
    }
    public List<DishDTO> toDtoList(List<Dish> dishes){
        return dishes.stream()
                    .map(this::toDto)
                    .toList();
    }
    public Dish toEntity(DishDTO dto){
        Dish dish = new Dish();
        dish.setName(dto.getName().trim());
        List<DishIngredient> dishIngredients = dishIngredientManager.toEntityList(dto.getDishIngredientDTOS(), dish);
        dish.setDishIngredients(dishIngredients);
        return dish;
    }
}
