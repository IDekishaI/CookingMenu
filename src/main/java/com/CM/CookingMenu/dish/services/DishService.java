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
import com.CM.CookingMenu.ingredient.entities.Ingredient;
import com.CM.CookingMenu.ingredient.services.IngredientService;
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
    private final IngredientService ingredientService;

    public List<DishDTO> getAllDishes() {
        return dishManager.toDtoList(dishRepo.findAllWithIngredients());
    }

    @Transactional
    public void saveDish(DishDTO dto) {
        if (dishRepo.existsByName(dto.getName().trim())) {
            throw new DishAlreadyExistsException(dto.getName().trim());
        }

        List<String> ingredientNames = dto.getDishIngredientDTOS().stream()
                .map(dishIngredientDTO -> dishIngredientDTO.ingredientName().trim())
                .toList();

        List<Ingredient> ingredients = ingredientService.findIngredientsByNames(ingredientNames);

        Dish dish = dishManager.toEntity(dto, ingredients);
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

        List<String> ingredientNames = dishDTO.getDishIngredientDTOS().stream()
                .map(di -> di.ingredientName().trim())
                .toList();

        List<Ingredient> ingredients = ingredientService.findIngredientsByNames(ingredientNames);

        dish.getDishIngredients().clear();
        List<DishIngredient> newDishIngredients = dishIngredientManager.toEntityList(dishDTO.getDishIngredientDTOS(), dish, ingredients);
        dish.getDishIngredients().addAll(newDishIngredients);

        dishRepo.save(dish);
    }
}
