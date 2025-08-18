package com.CM.CookingMenu.foodmenu.managers;

import com.CM.CookingMenu.dish.entities.Dish;
import com.CM.CookingMenu.dish.repositories.DishRepository;
import com.CM.CookingMenu.foodmenu.entities.FoodMenu;
import com.CM.CookingMenu.foodmenu.entities.FoodMenuDish;
import com.CM.CookingMenu.foodmenu.dtos.FoodMenuDishDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FoodMenuDishManager {
    private final DishRepository dishRepo;
    public FoodMenuDish toEntity(FoodMenuDishDTO dto, FoodMenu menu){
        if(menu == null)
            throw new IllegalArgumentException("FoodMenu cannot be null.");

        FoodMenuDish foodMenuDish = new FoodMenuDish();
        Dish dish = dishRepo.findByName(dto.getDishName().trim()).orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dish not found."));
        foodMenuDish.setMenu(menu);
        foodMenuDish.setDish(dish);
        return foodMenuDish;
    }
    public FoodMenuDishDTO toDto(FoodMenuDish foodMenuDish){
        if(foodMenuDish == null)
            throw new IllegalArgumentException("FoodMenu Dish cannot be null.");
        if(foodMenuDish.getDish() == null)
            throw new IllegalArgumentException("Dish in FoodMenuDish cannot be null.");

        FoodMenuDishDTO dto = new FoodMenuDishDTO();
        dto.setDishName(foodMenuDish.getDish().getName());
        return dto;
    }
    public List<FoodMenuDishDTO> toDtoList(List<FoodMenuDish> foodMenuDishes){
        if(foodMenuDishes == null)
            return new ArrayList<>();
        return foodMenuDishes.stream()
                        .filter(Objects::nonNull)
                        .map(this::toDto)
                        .toList();
    }
    public List<FoodMenuDish> toEntityList(List<FoodMenuDishDTO> dtoList, FoodMenu menu){
        if(menu == null)
            throw new IllegalArgumentException("FoodMenu cannot be null.");
        return dtoList.stream()
                    .filter(Objects::nonNull)
                    .map(dto -> toEntity(dto, menu))
                    .toList();
    }
}
