package com.CM.CookingMenu.Ingredient.repositories;

import com.CM.CookingMenu.Ingredient.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
}
