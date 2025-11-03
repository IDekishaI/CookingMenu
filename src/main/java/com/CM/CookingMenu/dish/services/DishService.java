package com.CM.CookingMenu.dish.services;

import com.CM.CookingMenu.dish.dtos.DishDTO;
import com.CM.CookingMenu.dish.entities.Dish;
import com.CM.CookingMenu.dish.entities.DishIngredient;
import com.CM.CookingMenu.dish.exceptions.DishAlreadyExistsException;
import com.CM.CookingMenu.dish.exceptions.DishInUseException;
import com.CM.CookingMenu.dish.exceptions.DishNotFoundException;
import com.CM.CookingMenu.dish.managers.DishIngredientManager;
import com.CM.CookingMenu.dish.managers.DishManager;
import com.CM.CookingMenu.dish.repositories.DishRepository;
import com.CM.CookingMenu.foodmenu.repositories.FoodMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new DishAlreadyExistsException(dto.getName().trim());
        }
        Dish dish = dishManager.toEntity(dto);
        dishRepo.save(dish);
    }

    @Transactional
    public void deleteDishByName(String name) {
        Dish dish = dishRepo.findByName(name.trim()).orElseThrow(() -> new DishNotFoundException(name));

        if (!foodMenuRepo.findByDishName(name.trim()).isEmpty())
            throw new DishInUseException(name.trim());

        dishRepo.delete(dish);
    }

    @Transactional
    public void updateDish(DishDTO dishDTO) {
        String dishName = dishDTO.getName().trim();

        Dish dish = dishRepo.findByName(dishName).orElseThrow(() -> new DishNotFoundException(dishName));

        dish.getDishIngredients().clear();
        List<DishIngredient> newDishIngredients = dishIngredientManager.toEntityList(dishDTO.getDishIngredientDTOS(), dish);
        dish.getDishIngredients().addAll(newDishIngredients);

        dishRepo.save(dish);
    }
}
