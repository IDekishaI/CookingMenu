package com.CM.CookingMenu.foodmenu.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Data transfer object for food menu")
public class FoodMenuDTO {
    @Schema(
            description = "Date of the food menu",
            example = "2025-10-30",
            requiredMode = Schema.RequiredMode.REQUIRED,
            pattern = "YYYY-MM-DD"
    )
    @NotNull(message = "Date cannot be null.")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format. Use YYYY-MM-DD")
    private String date;

    @Schema(
            description = "List of dishes included in this menu",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 1
    )
    @NotNull(message = "Food menu dishes cannot be null.")
    @NotEmpty
    @Valid
    List<FoodMenuDishDTO> foodMenuDishDTOS;

    @Schema(
            description = "Indicates if all dishes in this menu are suitable for fasting",
            example = "true",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean fastingSuitable;

    @Schema(
            description = "Number of users registered to attend this menu. Only visible to COOK and ADMIN roles.",
            example = "15",
            accessMode = Schema.AccessMode.READ_ONLY,
            nullable = true
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer attendeeCount;

    @Schema(
            description = "Whether the current authenticated user is attending this menu",
            example = "true",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean userAttending;
}
