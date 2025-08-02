package com.CM.CookingMenu.dish.controllers;

import com.CM.CookingMenu.dish.entities.DishDTO;
import com.CM.CookingMenu.dish.services.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/dishes")
public class DishController {
    private final DishService dishService;
    @GetMapping
    public ResponseEntity<List<DishDTO>> getAllDishes(){
        return ResponseEntity.ok(dishService.getAllDishes());
    }
}
