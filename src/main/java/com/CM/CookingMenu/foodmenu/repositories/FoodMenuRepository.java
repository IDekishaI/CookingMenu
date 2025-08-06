package com.CM.CookingMenu.foodmenu.repositories;

import com.CM.CookingMenu.foodmenu.entities.FoodMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface FoodMenuRepository extends JpaRepository<FoodMenu, Integer> {
    boolean existsByFoodmenuDate(LocalDate date);
}
