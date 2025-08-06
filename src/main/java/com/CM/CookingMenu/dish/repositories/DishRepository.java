package com.CM.CookingMenu.dish.repositories;

import com.CM.CookingMenu.dish.entities.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DishRepository extends JpaRepository<Dish, Integer> {
    boolean existsByName(String name);
    Optional<Dish> findByName(String name);
}
