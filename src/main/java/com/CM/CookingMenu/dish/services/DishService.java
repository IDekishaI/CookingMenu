package com.CM.CookingMenu.dish.services;

import com.CM.CookingMenu.dish.dtos.DishDTO;
import com.CM.CookingMenu.dish.entities.Dish;
import com.CM.CookingMenu.dish.entities.DishIngredient;
import com.CM.CookingMenu.dish.managers.DishIngredientManager;
import com.CM.CookingMenu.dish.managers.DishManager;
import com.CM.CookingMenu.dish.repositories.DishRepository;
import com.CM.CookingMenu.foodmenu.repositories.FoodMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DishService {
    private final DishManager dishManager;
    private final DishRepository dishRepo;
    private final DishIngredientManager dishIngredientManager;
    private final FoodMenuRepository foodMenuRepo;

    public List<DishDTO> getAllDishes() {
        return dishManager.toDtoList(dishRepo.findAllWithIngredients());
    }

    @Transactional
    public void saveDish(DishDTO dto) {
        if (dishRepo.existsByName(dto.getName().trim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dish name already exists.");
        }
        Dish dish = dishManager.toEntity(dto);
        dishRepo.save(dish);
    }

    @Transactional
    public void deleteDishByName(String name) {
        Dish dish = dishRepo.findByName(name.trim()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish with the name " + name + " not found"));

        if (!foodMenuRepo.findByDishName(name).isEmpty())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot remove dish. It is being used in existing Food Menus.");

        dishRepo.delete(dish);
    }

    @Transactional
    public void updateDish(DishDTO dishDTO) {
        String dishName = dishDTO.getName().trim();

        Dish dish = dishRepo.findByName(dishName).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish with the name " + dishName + " not found."));

        dish.getDishIngredients().clear();
        List<DishIngredient> newDishIngredients = dishIngredientManager.toEntityList(dishDTO.getDishIngredientDTOS(), dish);
        dish.getDishIngredients().addAll(newDishIngredients);

        dishRepo.save(dish);
    }
}
