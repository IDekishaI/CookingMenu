package com.CM.CookingMenu.ingredient.managers;

import com.CM.CookingMenu.ingredient.entities.Ingredient;
import com.CM.CookingMenu.ingredient.entities.IngredientDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class IngredientManager {
    public Ingredient toEntity(IngredientDTO dto){
        Ingredient ingredient = new Ingredient();
        ingredient.setName(dto.getName().trim());
        ingredient.setFastingSuitable(dto.getFastingSuitable());
        return ingredient;
    }
    public IngredientDTO toDto(Ingredient ingredient){
        if(ingredient == null)
            throw new IllegalArgumentException("Ingredient cannot be null.");

        return new IngredientDTO(ingredient.getName(), ingredient.isFastingSuitable());
    }
    public List<IngredientDTO> toDtoList(List<Ingredient> ingredients){
        if(ingredients == null)
            return new ArrayList<>();

        return ingredients.stream()
                            .filter(Objects::nonNull)
                            .map(this::toDto)
                            .toList();
    }
}
