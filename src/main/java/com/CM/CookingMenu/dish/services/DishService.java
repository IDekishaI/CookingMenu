package com.CM.CookingMenu.dish.services;

import com.CM.CookingMenu.dish.entities.Dish;
import com.CM.CookingMenu.dish.entities.DishDTO;
import com.CM.CookingMenu.dish.managers.DishManager;
import com.CM.CookingMenu.dish.repositories.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishManager dishManager;
    private final DishRepository dishRepo;
    public List<DishDTO> getAllDishes(){
        return dishManager.toDtoList(dishRepo.findAll());
    }
    public void saveDish(DishDTO dto){
        if(dishRepo.existsByName(dto.getName()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dish name already exists.");
        }
        Dish dish = dishManager.toEntity(dto);
        dishRepo.save(dish);
    }
}
