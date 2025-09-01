package com.CM.CookingMenu.foodmenu.repositories;

import com.CM.CookingMenu.foodmenu.entities.FoodMenuAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FoodMenuAttendanceRepository extends JpaRepository<FoodMenuAttendance, Integer> {
    int countByFoodmenuId(int foodmenuId);

    Optional<FoodMenuAttendance> findByFoodmenuIdAndUserId(int foodmenuId, int userId);

    boolean existsByFoodmenuIdAndUserId(int foodmenuId, int userId);

    void deleteByFoodmenuIdAndUserId(int foodmenuId, int userId);
}
