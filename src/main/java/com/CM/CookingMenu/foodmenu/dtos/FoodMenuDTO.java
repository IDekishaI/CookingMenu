package com.CM.CookingMenu.foodmenu.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodMenuDTO {
    @NotNull(message = "Date cannot be null.")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format. Use YYYY-MM-DD")
    private String date;

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
