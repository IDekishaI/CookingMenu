package com.CM.CookingMenu.foodmenu.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "Data transfer object for updating attendance")
public record AttendanceRequestDTO(
        @Schema(
                description = "Date of the Food Menu",
                example = "2025-10-30",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Date cannot be null.")
        LocalDate menuDate,

        @Schema(
                description = "Status of attendance",
                example = "false",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Attending status cannot be null.")
        Boolean attending
) {
}
