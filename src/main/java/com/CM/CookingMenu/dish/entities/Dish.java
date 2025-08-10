package com.CM.CookingMenu.dish.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="dish")
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dishId;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DishIngredient> dishIngredients;
    @Transient
    public boolean isFastingSuitable(){
        if(dishIngredients == null || dishIngredients.isEmpty())
            return true;

        for(DishIngredient di : dishIngredients){
            if(!di.getIngredient().isFastingSuitable())
                return false;
        }
        return true;
    }
}
