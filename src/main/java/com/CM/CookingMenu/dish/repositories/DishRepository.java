package com.CM.CookingMenu.dish.repositories;

import com.CM.CookingMenu.dish.entities.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Integer> {
}
