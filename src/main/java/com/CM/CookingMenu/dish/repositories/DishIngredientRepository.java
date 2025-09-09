package com.CM.CookingMenu.dish.repositories;

import com.CM.CookingMenu.dish.entities.DishIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DishIngredientRepository extends JpaRepository<DishIngredient, Integer> {
    @Query("SELECT COUNT(di) > 0 FROM DishIngredient di WHERE di.ingredient.name = :ingredientName")
    boolean existsByIngredientName(@Param("ingredientName") String ingredientName);
}
