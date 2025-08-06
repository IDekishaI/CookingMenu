package com.CM.CookingMenu.foodmenu.managers;

import com.CM.CookingMenu.foodmenu.entities.FoodMenu;
import com.CM.CookingMenu.foodmenu.entities.FoodMenuDTO;
import com.CM.CookingMenu.foodmenu.entities.FoodMenuDish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FoodMenuManager {
    private final FoodMenuDishManager foodMenuDishManager;

    public FoodMenuDTO toDto(FoodMenu foodMenu){
        FoodMenuDTO dto = new FoodMenuDTO();
        dto.setDate(foodMenu.getFoodmenuDate());
        dto.setFoodMenuDishDTOS(foodMenuDishManager.toDtoList(foodMenu.getDishes()));
        return dto;
    }
    public FoodMenu toEntity(FoodMenuDTO dto){
        FoodMenu foodMenu = new FoodMenu();
        List<FoodMenuDish> foodMenuDishes = foodMenuDishManager.toEntityList(dto.getFoodMenuDishDTOS(), foodMenu);
        foodMenu.setFoodmenuDate(dto.getDate());
        foodMenu.setDishes(foodMenuDishes);
        return foodMenu;
    }
    public List<FoodMenuDTO> toDtoList(List<FoodMenu> foodMenus){
        return foodMenus.stream()
                    .map(this::toDto)
                    .toList();
    }
}
