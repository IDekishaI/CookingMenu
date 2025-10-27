package com.CM.CookingMenu.dish.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for Dish")
public class DishDTO {
    @Schema(
            description = "Name of dish",
            example = "French Fries",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Dish name cannot be null.")
    @NotBlank(message = "Dish name cannot be blank.")
    @Size(min = 2, max = 100)
    String name;
    @Schema(
            description = "List of dish ingredients",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Dish Ingredients cannot be null.")
    @NotEmpty(message = "Must have at least 1 ingredient.")
    @Valid
    List<DishIngredientDTO> dishIngredientDTOS;
    @Schema(
            description = "Fasting suitability of the dish",
            example = "true",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean fastingSuitable;
}
