package com.CM.CookingMenu.Ingridient.services;

import com.CM.CookingMenu.Ingridient.managers.IngridientManager;
import com.CM.CookingMenu.Ingridient.repositories.IngridientRepository;
import com.CM.CookingMenu.Ingridient.entities.Ingridient;
import com.CM.CookingMenu.Ingridient.entities.IngridientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngridientService {
    private final IngridientRepository ingridientRepo;
    private final IngridientManager ingridientManager;
    public List<IngridientDTO> getAllIngridients(){
        return ingridientManager.toDtoList(ingridientRepo.findAll());
    }
    public void addIngridient(IngridientDTO dto){
        Ingridient ingridient = ingridientManager.toEntity(dto);
        ingridientRepo.save(ingridient);
    }
}
