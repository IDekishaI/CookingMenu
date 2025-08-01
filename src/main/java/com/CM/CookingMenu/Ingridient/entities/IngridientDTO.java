package com.CM.CookingMenu.Ingridient.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngridientDTO {
    private String name;
    private Double quantity;
}
