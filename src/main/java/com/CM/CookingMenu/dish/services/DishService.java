package com.CM.CookingMenu.dish.services;

import com.CM.CookingMenu.dish.entities.DishDTO;
import com.CM.CookingMenu.dish.managers.DishManager;
import com.CM.CookingMenu.dish.repositories.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishManager dishManager;
    private final DishRepository dishRepo;
    public List<DishDTO> getAllDishes(){
        return dishManager.toDtoList(dishRepo.findAll());
    }
}
