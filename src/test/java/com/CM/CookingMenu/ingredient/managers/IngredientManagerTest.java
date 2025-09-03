package com.CM.CookingMenu.ingredient.managers;


import com.CM.CookingMenu.ingredient.dtos.IngredientDTO;
import com.CM.CookingMenu.ingredient.entities.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Ingredient Manager Unit Tests")
public class IngredientManagerTest {

    private IngredientManager ingredientManager;
    private Ingredient tomatoEntity;
    private IngredientDTO tomatoDTO;

    @BeforeEach
    void setUp(){
        ingredientManager = new IngredientManager();

        tomatoEntity = new Ingredient();
        tomatoEntity.setIngredientId(1);
        tomatoEntity.setName("Tomato");
        tomatoEntity.setFastingSuitable(true);

        tomatoDTO = new IngredientDTO();
        tomatoDTO.setName("Tomato");
        tomatoDTO.setFastingSuitable(true);
    }
    /*toEntity success |
    * toentity trim success |
    * todto success |
    * todto exception if null |
    * todtolist success |
    * todtolist empty if list empty |
    * todtolist filter null |
    * */

    @Test
    @DisplayName("Should convert DTO to entity correctly")
    void shouldConvertDTOToEntityCorrectly(){
        Ingredient result = ingredientManager.toEntity(tomatoDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Tomato");
        assertThat(result.isFastingSuitable()).isEqualTo(true);
        assertThat(result.getIngredientId()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should successfully trim whitespace when converting to entity")
    void shouldSuccessfullyTrimWhiteSplaceWhenConvertingToEntity(){
        IngredientDTO dtoWithSpaces = new IngredientDTO("  Tomato  ", true);

        Ingredient result = ingredientManager.toEntity(dtoWithSpaces);

        assertThat(result.getName()).isEqualTo("Tomato");
    }

    @Test
    @DisplayName("Should convert entity to DTO correctly")
    void shouldConvertEntityToDTOCorrectly(){
        IngredientDTO result = ingredientManager.toDto(tomatoEntity);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Tomato");
        assertThat(result.getFastingSuitable()).isEqualTo(true);
    }

    @Test
    @DisplayName("shouldThrowExceptionConvertingNullEntityToDTO")
    void shouldThrowExceptionConvertingNullEntityToDTO(){
        assertThatException().isThrownBy(()->{
            ingredientManager.toDto(null);
        }).isInstanceOf(IllegalArgumentException.class)
                .withMessage("Ingredient cannot be null.");

    }

    @Test
    @DisplayName("Should successfully convert entity list to DTO List")
    void shouldSuccessfullyConvertEntityListToDTOList(){
        List<Ingredient> entityList = new ArrayList<>();
        Ingredient beefEntity = new Ingredient();
        beefEntity.setName("Beef");
        beefEntity.setFastingSuitable(false);
        entityList.add(tomatoEntity);
        entityList.add(beefEntity);

        List<IngredientDTO> result = ingredientManager.toDtoList(entityList);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Tomato");
        assertThat(result.get(0).getFastingSuitable()).isEqualTo(true);
        assertThat(result.get(1).getName()).isEqualTo("Beef");
        assertThat(result.get(1).getFastingSuitable()).isEqualTo(false);
    }

    @Test
    @DisplayName("Should return empty list if entity list is empty")
    void shouldReturnEmptyListIfEntityListIsEmpty(){
        List<Ingredient> entityList = new ArrayList<>();

        List<IngredientDTO> result = ingredientManager.toDtoList(entityList);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should filter null in entity list when converting to DTO list")
    void shouldFilterNullInEntityListWhenConvertingToDTOList(){
        List<Ingredient> entityList = Arrays.asList(null, tomatoEntity);
        List<IngredientDTO> result = ingredientManager.toDtoList(entityList);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Tomato");
    }
}
