package com.CM.CookingMenu.dish.managers;

import com.CM.CookingMenu.dish.entities.DishIngredient;
import com.CM.CookingMenu.dish.entities.DishIngredientDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DishIngredientManager {
    public DishIngredientDTO toDto(DishIngredient dishIngredient){
        DishIngredientDTO dto = new DishIngredientDTO();
        dto.setIngredientName(dishIngredient.getIngredient().getName());
        dto.setFastingSuitable(dishIngredient.getIngredient().isFastingSuitable());
        dto.setQuantity(dishIngredient.getQuantity());
        return dto;
    }
    public List<DishIngredientDTO> toDtoList(List<DishIngredient> dishIngredients){
        return dishIngredients.stream()
                            .map(this::toDto)
                            .toList();
    }
}
