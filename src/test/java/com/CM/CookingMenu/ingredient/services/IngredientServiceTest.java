package com.CM.CookingMenu.ingredient.services;

import com.CM.CookingMenu.ingredient.dtos.IngredientDTO;
import com.CM.CookingMenu.ingredient.entities.Ingredient;
import com.CM.CookingMenu.ingredient.managers.IngredientManager;
import com.CM.CookingMenu.ingredient.repositories.IngredientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("IngredientService Unit Tests")
public class IngredientServiceTest {
    @Mock
    private IngredientRepository ingredientRepo;

    @Mock
    private IngredientManager ingredientManager;

    @InjectMocks
    private IngredientService ingredientService;

    private IngredientDTO tomatoDTO;
    private IngredientDTO meatDTO;
    private Ingredient tomatoEntity;
    private Ingredient meatEntity;

    @BeforeEach
    void setUp() {
        tomatoDTO = new IngredientDTO("Tomato", true);
        meatDTO = new IngredientDTO("Beef", false);

        tomatoEntity = new Ingredient();
        tomatoEntity.setIngredientId(1);
        tomatoEntity.setName("Tomato");
        tomatoEntity.setFastingSuitable(true);

        meatEntity = new Ingredient();
        meatEntity.setIngredientId(2);
        meatEntity.setName("Beef");
        meatEntity.setFastingSuitable(false);
    }

    @Test
    @DisplayName("Should return all ingredients successfully")
    void shouldReturnAllIngredientsSuccessfully() {
        List<Ingredient> entities = Arrays.asList(tomatoEntity, meatEntity);
        List<IngredientDTO> expectedDTOs = Arrays.asList(tomatoDTO, meatDTO);

        when(ingredientRepo.findAll()).thenReturn(entities);
        when(ingredientManager.toDtoList(entities)).thenReturn(expectedDTOs);

        List<IngredientDTO> result = ingredientService.getAllIngredients();
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("Tomato");
        assertThat(result.get(0).fastingSuitable()).isEqualTo(true);
        assertThat(result.get(1).name()).isEqualTo("Beef");
        assertThat(result.get(1).fastingSuitable()).isEqualTo(false);

        verify(ingredientRepo).findAll();
        verify(ingredientManager).toDtoList(entities);
    }

    @Test
    @DisplayName("Should return empty list when there are no ingredients.")
    void shouldReturnEmptyListWhenNoIngredients() {
        List<Ingredient> emptyEntities = new ArrayList<>();
        List<IngredientDTO> emptyDTOs = new ArrayList<>();

        when(ingredientRepo.findAll()).thenReturn(emptyEntities);
        when(ingredientManager.toDtoList(emptyEntities)).thenReturn(emptyDTOs);

        List<IngredientDTO> result = ingredientService.getAllIngredients();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should add ingredient successfully when name is unique.")
    void shouldAddIngredientSuccessfully() {
        when(ingredientRepo.findByName("Tomato")).thenReturn(Optional.empty());
        when(ingredientManager.toEntity(tomatoDTO)).thenReturn(tomatoEntity);
        when(ingredientRepo.save(tomatoEntity)).thenReturn(tomatoEntity);

        assertThatNoException().isThrownBy(()->{
            ingredientService.addIngredient(tomatoDTO);
        });

        verify(ingredientRepo).findByName("Tomato");
        verify(ingredientManager).toEntity(tomatoDTO);
        verify(ingredientRepo).save(tomatoEntity);
    }

    @Test
    @DisplayName("Should throw exception when ingredient name already exists")
    void shouldThrowExceptionWhenIngredientNameAlreadyExists() {
        when(ingredientRepo.findByName("Tomato")).thenReturn(Optional.of(tomatoEntity));

        Throwable exception = catchThrowable(() -> ingredientService.addIngredient(tomatoDTO));

        assertThat(exception).isInstanceOf(ResponseStatusException.class);

        ResponseStatusException responseException = (ResponseStatusException) exception;

        assertThat(responseException.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseException.getReason()).isEqualTo("Ingredient name already exists.");

        verify(ingredientRepo).findByName("Tomato");
        verify(ingredientRepo, never()).save(any(Ingredient.class));
        verify(ingredientManager, never()).toEntity(any(IngredientDTO.class));
    }
    @Test
    @DisplayName("Should handle ingredient name with whitespaces correctly")
    void shouldHandleIngredientNameWithWhitespacesCorrectly(){
        IngredientDTO dtoWithSpaces = new IngredientDTO("  Tomato  ", true);

        when(ingredientRepo.findByName("Tomato")).thenReturn(Optional.empty());
        when(ingredientManager.toEntity(dtoWithSpaces)).thenReturn(tomatoEntity);
        when(ingredientRepo.save(any(Ingredient.class))).thenReturn(tomatoEntity);

        assertThatNoException().isThrownBy(()->{
            ingredientService.addIngredient(dtoWithSpaces);
        });

        verify(ingredientRepo).findByName("Tomato");
    }
}
