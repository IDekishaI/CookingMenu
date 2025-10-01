package com.CM.CookingMenu.foodmenu.controllers;

import com.CM.CookingMenu.foodmenu.ai.dtos.FoodMenuSuggestionRequestDTO;
import com.CM.CookingMenu.foodmenu.ai.dtos.FoodMenuSuggestionResponseDTO;
import com.CM.CookingMenu.foodmenu.ai.services.FoodMenuSuggestionService;
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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/menus")
@Validated
public class FoodMenuController {
    private final FoodMenuService foodMenuService;
    private final FoodMenuSuggestionService foodMenuSuggestionService;

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
    @DeleteMapping("/delete/{menuDate}")
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<String> deleteMenu(@PathVariable
                                                 @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format. Use YYYY-MM-DD")
                                                 @NotBlank(message = "Date cannot be blank.")String menuDate){
        LocalDate date;
        try{
            date = LocalDate.parse(menuDate);
        }
        catch (DateTimeParseException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format: " + menuDate);
        }

        foodMenuService.deleteFoodmenuByDate(date);

        return ResponseEntity.ok("Foodmenu deleted successfully.");
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<String> updateFoodmenu(@RequestBody @Valid FoodMenuDTO foodmenuDTO){
        foodMenuService.updateFoodmenu(foodmenuDTO);
        return ResponseEntity.ok("Successfully updated the foodmenu.");
    }

    @PostMapping("/attend")
    @PreAuthorize("hasAnyRole('USER', 'COOK', 'ADMIN')")
    public ResponseEntity<String> attendMenu(@Valid @RequestBody AttendanceRequestDTO dto){
        String message = foodMenuService.updateAttendance(dto);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/generate")
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<FoodMenuSuggestionResponseDTO> getFoodMenuSuggestions(@Valid @RequestBody FoodMenuSuggestionRequestDTO dto){
        FoodMenuSuggestionResponseDTO suggestions = foodMenuSuggestionService.generateFoodMenuSuggestion(dto);
        return ResponseEntity.ok(suggestions);
    }
}
