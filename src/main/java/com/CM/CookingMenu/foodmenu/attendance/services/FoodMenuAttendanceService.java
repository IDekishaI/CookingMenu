package com.CM.CookingMenu.foodmenu.attendance.services;

import com.CM.CookingMenu.auth.entities.User;
import com.CM.CookingMenu.common.utils.SecurityUtils;
import com.CM.CookingMenu.foodmenu.attendance.dtos.AttendanceRequestDTO;
import com.CM.CookingMenu.foodmenu.attendance.entities.FoodMenuAttendance;
import com.CM.CookingMenu.foodmenu.attendance.repositories.FoodMenuAttendanceRepository;
import com.CM.CookingMenu.foodmenu.entities.FoodMenu;
import com.CM.CookingMenu.foodmenu.exceptions.FoodMenuNotFoundException;
import com.CM.CookingMenu.foodmenu.repositories.FoodMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Service
@RequiredArgsConstructor
public class FoodMenuAttendanceService {

    private final FoodMenuRepository foodMenuRepo;
    private final FoodMenuAttendanceRepository attendanceRepo;

    @Transactional
    public String updateAttendance(AttendanceRequestDTO dto) {
        User currentUser = SecurityUtils.getCurrentUser();
        FoodMenu menu = foodMenuRepo.findByFoodmenuDate(dto.menuDate()).orElseThrow(() -> new FoodMenuNotFoundException(dto.menuDate().toString()));
        boolean isCurrentlyAttending = attendanceRepo.existsByFoodmenuIdAndUserId(menu.getFoodMenuId(), currentUser.getUserId());
        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime menuDate = LocalDateTime.of(dto.menuDate(), LocalTime.MIN);
        LocalDateTime minApplyDate;
        if (menuDate.getDayOfWeek().equals(DayOfWeek.MONDAY))
            minApplyDate = menuDate.minusHours(51);
        else
            minApplyDate = menuDate.minusHours(3);
        if (timeNow.isAfter(minApplyDate)) {
            return "You had to apply before 21:00 last weekday before the menu.";
        }
        if (dto.attending()) {
            if (isCurrentlyAttending)
                return "You are already registered for this menu.";
            else {
                FoodMenuAttendance foodMenuAttendance = new FoodMenuAttendance();
                foodMenuAttendance.setFoodmenuId(menu.getFoodMenuId());
                foodMenuAttendance.setUserId(currentUser.getUserId());
                attendanceRepo.save(foodMenuAttendance);
                return "Successfully registered for this menu.";
            }
        } else {
            if (isCurrentlyAttending) {
                attendanceRepo.deleteByFoodmenuIdAndUserId(menu.getFoodMenuId(), currentUser.getUserId());
                return "Successfully cancelled your attendance.";
            } else
                return "You were not registered for this menu.";
        }
    }
}
