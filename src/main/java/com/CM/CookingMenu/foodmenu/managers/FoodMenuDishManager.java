package com.CM.CookingMenu.foodmenu.managers;

import com.CM.CookingMenu.dish.entities.Dish;
import com.CM.CookingMenu.dish.repositories.DishRepository;
import com.CM.CookingMenu.foodmenu.entities.FoodMenu;
import com.CM.CookingMenu.foodmenu.entities.FoodMenuDish;
import com.CM.CookingMenu.foodmenu.entities.FoodMenuDishDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FoodMenuDishManager {
    private final DishRepository dishRepo;
    public FoodMenuDish toEntity(FoodMenuDishDTO dto, FoodMenu menu){
        FoodMenuDish foodMenuDish = new FoodMenuDish();
        Dish dish = dishRepo.findByName(dto.getDishName().trim()).orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dish not found."));
        foodMenuDish.setMenu(menu);
        foodMenuDish.setDish(dish);
        return foodMenuDish;
    }
    public FoodMenuDishDTO toDto(FoodMenuDish foodMenuDish){
        FoodMenuDishDTO dto = new FoodMenuDishDTO();
        dto.setDishName(foodMenuDish.getDish().getName());
        return dto;
    }
    public List<FoodMenuDishDTO> toDtoList(List<FoodMenuDish> foodMenuDishes){
        return foodMenuDishes.stream()
                        .map(this::toDto)
                        .toList();
    }
    public List<FoodMenuDish> toEntityList(List<FoodMenuDishDTO> dtoList, FoodMenu menu){
        return dtoList.stream()
                    .map(dto -> toEntity(dto, menu))
                    .toList();
    }
}
