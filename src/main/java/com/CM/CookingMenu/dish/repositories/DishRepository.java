package com.CM.CookingMenu.dish.repositories;

import com.CM.CookingMenu.dish.entities.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DishRepository extends JpaRepository<Dish, Integer> {
    @Query("Select DISTINCT d FROM Dish d " +
            "LEFT JOIN FETCH d.dishIngredients di " +
            "LEFT JOIN FETCH di.ingredient i")
    List<Dish> findAllWithIngredients();
    boolean existsByName(String name);
    Optional<Dish> findByName(String name);
}
