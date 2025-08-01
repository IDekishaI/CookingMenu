package com.CM.CookingMenu.Ingridient.managers;

import com.CM.CookingMenu.Ingridient.entities.Ingridient;
import com.CM.CookingMenu.Ingridient.entities.IngridientDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IngridientManager {
    public Ingridient toEntity(IngridientDTO dto){
        Ingridient ingridient = new Ingridient();
        ingridient.setName(dto.getName());
        ingridient.setQuantity(dto.getQuantity());
        return ingridient;
    }
    public IngridientDTO toDto(Ingridient ingridient){
        IngridientDTO dto = new IngridientDTO();
        return new IngridientDTO(ingridient.getName(), ingridient.getQuantity());
    }
    public List<IngridientDTO> toDtoList(List<Ingridient> ingridients){
        return ingridients.stream()
                            .map(this::toDto)
                            .toList();
    }
}
