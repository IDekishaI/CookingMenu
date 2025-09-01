package com.CM.CookingMenu.foodmenu.repositories;

import com.CM.CookingMenu.foodmenu.entities.FoodMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FoodMenuRepository extends JpaRepository<FoodMenu, Integer> {
    boolean existsByFoodmenuDate(LocalDate date);

    Optional<FoodMenu> findByFoodmenuDate(LocalDate date);
    @Query("Select DISTINCT fm FROM FoodMenu fm " +
            "WHERE fm.foodmenuDate > CURRENT_DATE")
    List<FoodMenu> findAllFutureMenus();
    @Query("Select DISTINCT fm FROM FoodMenu fm "+
            "LEFT JOIN FETCH fm.dishes fmd " +
            "LEFT JOIN FETCH fmd.dish d")
    List<FoodMenu> findAllWithDishes();
    @Query("SELECT DISTINCT fm FROM FoodMenu fm " +
            "JOIN fm.dishes fmd " +
            "JOIN fmd.dish d " +
            "WHERE LOWER(d.name) = LOWER(:dishName)")
    List<FoodMenu> findByDishName(@Param("dishName") String dishName);
}
