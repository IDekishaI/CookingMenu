package com.CM.CookingMenu.ingredient.repositories;

import com.CM.CookingMenu.ingredient.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
}
