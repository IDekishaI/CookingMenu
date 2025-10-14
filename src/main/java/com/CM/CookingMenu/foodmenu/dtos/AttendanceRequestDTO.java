package com.CM.CookingMenu.foodmenu.dtos;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AttendanceRequestDTO (
    @NotNull(message = "Date cannot be null.")
    LocalDate menuDate,

    @NotNull(message = "Attending status cannot be null.")
    Boolean attending
){}
