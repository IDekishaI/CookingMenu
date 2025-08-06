package com.CM.CookingMenu.foodmenu.services;

import com.CM.CookingMenu.foodmenu.entities.FoodMenu;
import com.CM.CookingMenu.foodmenu.entities.FoodMenuDTO;
import com.CM.CookingMenu.foodmenu.managers.FoodMenuManager;
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
public class FoodMenuService {
    private final FoodMenuRepository foodMenuRepo;
    private final FoodMenuManager foodMenuManager;
    public List<FoodMenuDTO> getAllFoodMenus(){
       return foodMenuManager.toDtoList(foodMenuRepo.findAll());
    }
    @Transactional
    public void saveFoodmenu(FoodMenuDTO foodMenuDTO){
        if(foodMenuRepo.existsByFoodmenuDate(foodMenuDTO.getDate())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Foodmenu Date already exists.");
        }
        FoodMenu foodmenu = foodMenuManager.toEntity(foodMenuDTO);
        foodMenuRepo.save(foodmenu);
    }
}
