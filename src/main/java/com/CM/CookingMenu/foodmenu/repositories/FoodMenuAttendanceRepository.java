package com.CM.CookingMenu.foodmenu.repositories;

import com.CM.CookingMenu.foodmenu.entities.FoodMenuAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FoodMenuAttendanceRepository extends JpaRepository<FoodMenuAttendance, Integer> {
    int countByFoodmenuId(int foodmenuId);

    Optional<FoodMenuAttendance> findByFoodmenuIdAndUserId(int foodmenuId, int userId);

    boolean existsByFoodmenuIdAndUserId(int foodmenuId, int userId);

    void deleteByFoodmenuIdAndUserId(int foodmenuId, int userId);

    @Modifying
    @Query("DELETE FROM FoodMenuAttendance fma WHERE fma.foodmenuId = :foodmenuId")
    void deleteByFoodmenuId(@Param("foodmenuId") int foodmenuId);
}
