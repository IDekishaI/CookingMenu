package com.CM.CookingMenu.dish.controllers;

import com.CM.CookingMenu.dish.entities.DishDTO;
import com.CM.CookingMenu.dish.managers.DishManager;
import com.CM.CookingMenu.dish.services.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/dishes")
public class DishController {
    private final DishManager dishManager;
    private final DishService dishService;
    @GetMapping
    public ResponseEntity<List<DishDTO>> getAllDishes(){
        return ResponseEntity.ok(dishService.getAllDishes());
    }
    @PostMapping
    public ResponseEntity<String> saveDish(@RequestBody DishDTO dto){
        dishService.saveDish(dto);
        return ResponseEntity.ok("Dish Added.");
    }
}
