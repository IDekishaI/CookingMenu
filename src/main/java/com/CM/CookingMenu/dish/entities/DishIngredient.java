package com.CM.CookingMenu.dish.entities;

import com.CM.CookingMenu.ingredient.entities.Ingredient;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dish_ingredients")
public class DishIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int dishIngredientId;

    @ManyToOne
    @JoinColumn(name = "dishId")
    @JsonBackReference
    private Dish dish;

    @ManyToOne
    @JoinColumn(name = "ingredientId")
    private Ingredient ingredient;

    private double quantity;
}
