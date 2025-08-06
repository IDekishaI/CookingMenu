package com.CM.CookingMenu.foodmenu.entities;

import com.CM.CookingMenu.dish.entities.Dish;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "foodmenu_dishes")
public class FoodMenuDish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int foodmenuDishesId;

    @ManyToOne
    @JoinColumn(name = "foodmenuId")
    @JsonBackReference
    private FoodMenu menu;

    @ManyToOne
    @JoinColumn(name = "dishId")
    private Dish dish;
}
