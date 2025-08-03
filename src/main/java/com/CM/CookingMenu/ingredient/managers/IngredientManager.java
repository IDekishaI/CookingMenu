package com.CM.CookingMenu.ingredient.managers;

import com.CM.CookingMenu.ingredient.entities.Ingredient;
import com.CM.CookingMenu.ingredient.entities.IngredientDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IngredientManager {
    public Ingredient toEntity(IngredientDTO dto){
        Ingredient ingredient = new Ingredient();
        ingredient.setName(dto.getName().trim());
        ingredient.setFastingSuitable(dto.getFastingSuitable());
        return ingredient;
    }
    public IngredientDTO toDto(Ingredient ingredient){
        IngredientDTO dto = new IngredientDTO();
        return new IngredientDTO(ingredient.getName(), ingredient.isFastingSuitable());
    }
    public List<IngredientDTO> toDtoList(List<Ingredient> ingredients){
        return ingredients.stream()
                            .map(this::toDto)
                            .toList();
    }
}
