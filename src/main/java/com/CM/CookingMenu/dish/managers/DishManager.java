package com.CM.CookingMenu.dish.managers;

import com.CM.CookingMenu.dish.dtos.DishDTO;
import com.CM.CookingMenu.dish.entities.Dish;
import com.CM.CookingMenu.dish.entities.DishIngredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DishManager {
    private final DishIngredientManager dishIngredientManager;

    public DishDTO toDto(Dish dish) {
        if (dish == null)
            throw new IllegalArgumentException("Dish cannot be null.");

        DishDTO dto = new DishDTO();
        dto.setName(dish.getName());
        dto.setDishIngredientDTOS(dishIngredientManager.toDtoList(dish.getDishIngredients()));
        dto.setFastingSuitable(dish.isFastingSuitable());
        return dto;
    }

    public List<DishDTO> toDtoList(List<Dish> dishes) {
        if (dishes == null)
            return new ArrayList<>();

        return dishes.stream()
                .filter(Objects::nonNull)
                .map(this::toDto)
                .toList();
    }

    public Dish toEntity(DishDTO dto) {
        Dish dish = new Dish();
        dish.setName(dto.getName().trim());
        List<DishIngredient> dishIngredients = dishIngredientManager.toEntityList(dto.getDishIngredientDTOS(), dish);
        dish.setDishIngredients(dishIngredients);
        return dish;
    }
}
