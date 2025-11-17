package com.CM.CookingMenu.foodmenu.services;

import com.CM.CookingMenu.common.utils.DateUtils;
import com.CM.CookingMenu.common.utils.SecurityUtils;
import com.CM.CookingMenu.foodmenu.attendance.repositories.FoodMenuAttendanceRepository;
import com.CM.CookingMenu.foodmenu.dtos.FoodMenuDTO;
import com.CM.CookingMenu.foodmenu.entities.FoodMenu;
import com.CM.CookingMenu.foodmenu.entities.FoodMenuDish;
import com.CM.CookingMenu.foodmenu.exceptions.FoodMenuAlreadyExistsException;
import com.CM.CookingMenu.foodmenu.exceptions.FoodMenuNotFoundException;
import com.CM.CookingMenu.foodmenu.managers.FoodMenuDishManager;
import com.CM.CookingMenu.foodmenu.managers.FoodMenuManager;
import com.CM.CookingMenu.foodmenu.repositories.FoodMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FoodMenuService {
    private final FoodMenuRepository foodMenuRepo;
    private final FoodMenuManager foodMenuManager;
    private final FoodMenuDishManager foodMenuDishManager;
    private final FoodMenuAttendanceRepository attendanceRepo;

    public List<FoodMenuDTO> getAllFoodMenus() {
        return foodMenuManager.toDtoListWithAttendance(foodMenuRepo.findAllWithDishes(), SecurityUtils.getCurrentUser(), SecurityUtils.isCookOrAdmin());
    }

    public List<FoodMenuDTO> getAllFoodMenusContainingDish(String dishName) {
        return foodMenuManager.toDtoListWithAttendance(foodMenuRepo.findByDishName(dishName), SecurityUtils.getCurrentUser(), SecurityUtils.isCookOrAdmin());
    }

    public List<FoodMenuDTO> getAllFutureMenus() {
        return foodMenuManager.toDtoListWithAttendance(foodMenuRepo.findAllFutureMenus(), SecurityUtils.getCurrentUser(), SecurityUtils.isCookOrAdmin());
    }

    @Transactional
    public void saveFoodmenu(FoodMenuDTO foodMenuDTO) {

        if (foodMenuRepo.existsByFoodmenuDate(DateUtils.parseStringToLocalDate(foodMenuDTO.getDate()))) {
            throw new FoodMenuAlreadyExistsException(foodMenuDTO.getDate());
        }
        FoodMenu foodmenu = foodMenuManager.toEntity(foodMenuDTO);
        foodMenuRepo.save(foodmenu);
    }

    @Transactional
    public void deleteFoodmenuByDate(LocalDate date) {
        FoodMenu foodMenu = foodMenuRepo.findByFoodmenuDate(date).orElseThrow(() -> new FoodMenuNotFoundException(date.toString()));
        attendanceRepo.deleteByFoodmenuId(foodMenu.getFoodMenuId());
        foodMenuRepo.delete(foodMenu);
    }

    @Transactional
    public void updateFoodmenu(FoodMenuDTO dto) {
        LocalDate date = DateUtils.parseStringToLocalDate(dto.getDate());

        FoodMenu foodMenu = foodMenuRepo.findByFoodmenuDate(date).orElseThrow(() -> new FoodMenuNotFoundException(date.toString()));

        foodMenu.getDishes().clear();
        List<FoodMenuDish> newFoodmenuDishes = foodMenuDishManager.toEntityList(dto.getFoodMenuDishDTOS(), foodMenu);
        foodMenu.getDishes().addAll(newFoodmenuDishes);

        foodMenuRepo.save(foodMenu);
    }

}
