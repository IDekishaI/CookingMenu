package com.CM.CookingMenu.foodmenu.services;

import com.CM.CookingMenu.auth.entities.User;
import com.CM.CookingMenu.foodmenu.dtos.AttendanceRequestDTO;
import com.CM.CookingMenu.foodmenu.entities.FoodMenu;
import com.CM.CookingMenu.foodmenu.dtos.FoodMenuDTO;
import com.CM.CookingMenu.foodmenu.entities.FoodMenuAttendance;
import com.CM.CookingMenu.foodmenu.entities.FoodMenuDish;
import com.CM.CookingMenu.foodmenu.managers.FoodMenuDishManager;
import com.CM.CookingMenu.foodmenu.managers.FoodMenuManager;
import com.CM.CookingMenu.foodmenu.repositories.FoodMenuAttendanceRepository;
import com.CM.CookingMenu.foodmenu.repositories.FoodMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FoodMenuService {
    private final FoodMenuRepository foodMenuRepo;
    private final FoodMenuManager foodMenuManager;
    private final FoodMenuDishManager foodMenuDishManager;
    private final FoodMenuAttendanceRepository attendanceRepo;
    public List<FoodMenuDTO> getAllFoodMenus(){
       return foodMenuManager.toDtoListWithAttendance(foodMenuRepo.findAllWithDishes(), getCurrentUser(), isCookOrAdmin());
    }
    public List<FoodMenuDTO> getAllFoodMenusContainingDish(String dishName){
        return foodMenuManager.toDtoListWithAttendance(foodMenuRepo.findByDishName(dishName), getCurrentUser(), isCookOrAdmin());
    }
    public List<FoodMenuDTO> getAllFutureMenus(){
        return foodMenuManager.toDtoListWithAttendance(foodMenuRepo.findAllFutureMenus(), getCurrentUser(), isCookOrAdmin());
    }
    @Transactional
    public void saveFoodmenu(FoodMenuDTO foodMenuDTO){

        if(foodMenuRepo.existsByFoodmenuDate(foodMenuManager.stringToLocalDate(foodMenuDTO.getDate()))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Foodmenu Date already exists.");
        }
        FoodMenu foodmenu = foodMenuManager.toEntity(foodMenuDTO);
        foodMenuRepo.save(foodmenu);
    }

    @Transactional
    public void deleteFoodmenuByDate(LocalDate date){
        FoodMenu foodMenu = foodMenuRepo.findByFoodmenuDate(date).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find a foodmenu on the date " + date.toString()));
        attendanceRepo.deleteByFoodmenuId(foodMenu.getFoodMenuId());
        foodMenuRepo.delete(foodMenu);
    }

    @Transactional
    public void updateFoodmenu(FoodMenuDTO dto){
        LocalDate date = foodMenuManager.stringToLocalDate(dto.getDate());

        FoodMenu foodMenu = foodMenuRepo.findByFoodmenuDate(date).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Couldn't find a foodmenu on the date " + date.toString()));

        foodMenu.getDishes().clear();
        List<FoodMenuDish> newFoodmenuDishes = foodMenuDishManager.toEntityList(dto.getFoodMenuDishDTOS(), foodMenu);
        foodMenu.getDishes().addAll(newFoodmenuDishes);

        foodMenuRepo.save(foodMenu);
    }

    private User getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }
    private Boolean isCookOrAdmin(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_COOK") ||
                                                                    authority.getAuthority().equals("ROLE_ADMIN")
                                                        );
    }

    @Transactional
    public String updateAttendance(AttendanceRequestDTO dto){
        User currentUser = getCurrentUser();
        FoodMenu menu = foodMenuRepo.findByFoodmenuDate(dto.menuDate()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Menu not found for specific date"));
        boolean isCurrentlyAttending = attendanceRepo.existsByFoodmenuIdAndUserId(menu.getFoodMenuId(), currentUser.getUserId());
        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime menuDate = LocalDateTime.of(dto.menuDate(), LocalTime.MIN);
        LocalDateTime minApplyDate;
        if(menuDate.getDayOfWeek().equals(DayOfWeek.MONDAY))
            minApplyDate = menuDate.minusHours(51);
        else
            minApplyDate = menuDate.minusHours(3);
        if(timeNow.isAfter(minApplyDate)){
            return "You had to apply before 21:00 last weekday before the menu.";
        }
        if(dto.attending()){
            if(isCurrentlyAttending)
                return "You are already registered for this menu.";
            else{
                FoodMenuAttendance foodMenuAttendance = new FoodMenuAttendance();
                foodMenuAttendance.setFoodmenuId(menu.getFoodMenuId());
                foodMenuAttendance.setUserId(currentUser.getUserId());
                attendanceRepo.save(foodMenuAttendance);
                return "Successfully registered for this menu.";
            }
        }
        else{
            if(isCurrentlyAttending){
                attendanceRepo.deleteByFoodmenuIdAndUserId(menu.getFoodMenuId(), currentUser.getUserId());
                return "Successfully cancelled your attendance.";
            }
            else
                return "You were not registered for this menu.";
        }
    }
}
