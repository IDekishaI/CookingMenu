package com.CM.CookingMenu.foodmenu.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodMenuDTO {
    @NotNull(message = "Date cannot be null.")
    private LocalDate date;

    @NotNull(message = "Food menu dishes cannot be null.")
    @NotEmpty
    @Valid
    List<FoodMenuDishDTO> foodMenuDishDTOS;


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean fastingSuitable;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer attendeeCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean userAttending;
}
