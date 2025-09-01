package com.CM.CookingMenu.foodmenu.controllers;

import com.CM.CookingMenu.foodmenu.dtos.FoodMenuDTO;
import com.CM.CookingMenu.foodmenu.dtos.AttendanceRequestDTO;
import com.CM.CookingMenu.foodmenu.services.FoodMenuService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/menus")
@Validated
public class FoodMenuController {
    private final FoodMenuService foodMenuService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'COOK', 'ADMIN')")
    public ResponseEntity<List<FoodMenuDTO>> getAllFutureMenus(){
        return ResponseEntity.ok(foodMenuService.getAllFutureMenus());
    }
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<List<FoodMenuDTO>> getAllFoodMenus(){
        return ResponseEntity.ok(foodMenuService.getAllFoodMenus());
    }
    @GetMapping("/{dishName}")
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<List<FoodMenuDTO>> getAllFoodMenusContainingDish(@PathVariable
                                                                           @Pattern(regexp = "^[a-zA-Z\\s]{2,100}$", message = "Invalid dish name format")
                                                                           @NotBlank(message = "Dish name cannot be blank.")
                                                                           String dishName){
        if(dishName == null || dishName.trim().isBlank())
            throw new IllegalArgumentException("Dish name cannot be null or empty");
        return ResponseEntity.ok(foodMenuService.getAllFoodMenusContainingDish(dishName.trim()));
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<String> saveFoodmenu(@Valid @RequestBody FoodMenuDTO dto){
        foodMenuService.saveFoodmenu(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Foodmenu added.");
    }

    @PostMapping("/attend")
    @PreAuthorize("hasAnyRole('USER', 'COOK', 'ADMIN')")
    public ResponseEntity<String> attendMenu(@Valid @RequestBody AttendanceRequestDTO dto){
        String message = foodMenuService.updateAttendance(dto);
        return ResponseEntity.ok(message);
    }
}
