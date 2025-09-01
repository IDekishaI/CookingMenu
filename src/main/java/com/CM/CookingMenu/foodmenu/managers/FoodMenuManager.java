package com.CM.CookingMenu.foodmenu.managers;

import com.CM.CookingMenu.auth.entities.User;
import com.CM.CookingMenu.foodmenu.entities.FoodMenu;
import com.CM.CookingMenu.foodmenu.dtos.FoodMenuDTO;
import com.CM.CookingMenu.foodmenu.entities.FoodMenuDish;
import com.CM.CookingMenu.foodmenu.repositories.FoodMenuAttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FoodMenuManager {
    private final FoodMenuDishManager foodMenuDishManager;
    private final FoodMenuAttendanceRepository attendanceRepo;

    public FoodMenuDTO toDto(FoodMenu foodMenu, User currentUser, boolean showAttendeeCount){
        if(foodMenu == null)
            throw new IllegalArgumentException("FoodMenu cannot be null.");

        FoodMenuDTO dto = new FoodMenuDTO();
        dto.setDate(foodMenu.getFoodmenuDate());
        dto.setFoodMenuDishDTOS(foodMenuDishManager.toDtoList(foodMenu.getDishes()));
        dto.setFastingSuitable(foodMenu.isFastingSuitable());

        if(currentUser != null){
            boolean userAttending = attendanceRepo.existsByFoodmenuIdAndUserId(foodMenu.getFoodMenuId(), currentUser.getUserId());
            dto.setUserAttending(userAttending);
        }

        if(showAttendeeCount){
            int count = attendanceRepo.countByFoodmenuId(foodMenu.getFoodMenuId());
            dto.setAttendeeCount(count);
        }

        return dto;
    }
    public FoodMenu toEntity(FoodMenuDTO dto){
        FoodMenu foodMenu = new FoodMenu();
        List<FoodMenuDish> foodMenuDishes = foodMenuDishManager.toEntityList(dto.getFoodMenuDishDTOS(), foodMenu);
        foodMenu.setFoodmenuDate(dto.getDate());
        foodMenu.setDishes(foodMenuDishes);
        return foodMenu;
    }
    public List<FoodMenuDTO> toDtoListWithAttendance(List<FoodMenu> foodMenus, User currentUser, boolean showAttendeeCount){
        if(foodMenus == null)
            return new ArrayList<>();
        return foodMenus.stream()
                        .filter(Objects::nonNull)
                        .map(menu -> toDto(menu, currentUser, showAttendeeCount))
                        .toList();
    }
    public List<FoodMenuDTO> toDtoList(List<FoodMenu> foodMenus){
        return toDtoListWithAttendance(foodMenus, null, false);
    }
}
