package com.CM.CookingMenu.foodmenu.controllers;

import com.CM.CookingMenu.foodmenu.entities.FoodMenu;
import com.CM.CookingMenu.foodmenu.entities.FoodMenuDTO;
import com.CM.CookingMenu.foodmenu.services.FoodMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/menus")
public class FoodMenuController {
    private final FoodMenuService foodMenuService;
    @GetMapping
    public ResponseEntity<List<FoodMenuDTO>> getAllFoodMenus(){
        return ResponseEntity.ok(foodMenuService.getAllFoodMenus());
    }
    @PostMapping
    public ResponseEntity<String> saveFoodmenu(@RequestBody FoodMenuDTO dto){
        foodMenuService.saveFoodmenu(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Foodmenu added.");
    }
}
