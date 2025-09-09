package com.CM.CookingMenu.dish.controllers;

import com.CM.CookingMenu.dish.dtos.DishDTO;
import com.CM.CookingMenu.dish.services.DishService;
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
@RequestMapping("/dishes")
@Validated
public class DishController {
    private final DishService dishService;
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'COOK', 'ADMIN')")
    public ResponseEntity<List<DishDTO>> getAllDishes(){
        return ResponseEntity.ok(dishService.getAllDishes());
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<String> saveDish(@Valid @RequestBody DishDTO dto){
        dishService.saveDish(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Dish Added.");
    }

    @DeleteMapping("/delete/{name}")
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<String> deleteDish(@PathVariable
                                                 @Pattern(regexp = "^[a-zA-Z_\\s]{2,100}$", message = "Invalid Dish name format")
                                                 @NotBlank(message = "Dish name cannot be blank.")String name){
        if(name == null || name.trim().isBlank())
            throw new IllegalArgumentException("Dish name cannot be null or blank.");

        dishService.deleteDishByName(name);

        return ResponseEntity.ok("Dish deleted successfully.");
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<String> updateDish(@Valid @RequestBody DishDTO dishDTO){
        dishService.updateDish(dishDTO);
        return ResponseEntity.ok("Dish updated successfully.");
    }
}
