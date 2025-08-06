package com.CM.CookingMenu.dish.controllers;

import com.CM.CookingMenu.dish.entities.DishDTO;
import com.CM.CookingMenu.dish.services.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/dishes")
@Validated
public class DishController {
    private final DishService dishService;
    @GetMapping
    public ResponseEntity<List<DishDTO>> getAllDishes(){
        return ResponseEntity.ok(dishService.getAllDishes());
    }
    @PostMapping
    public ResponseEntity<String> saveDish(@Valid @RequestBody DishDTO dto){
        dishService.saveDish(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Dish Added.");
    }
}
