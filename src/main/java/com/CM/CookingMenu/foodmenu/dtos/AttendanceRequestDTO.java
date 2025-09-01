package com.CM.CookingMenu.foodmenu.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequestDTO {
    @NotNull(message = "Date cannot be null.")
    private LocalDate menuDate;

    @NotNull(message = "Attending status cannot be null.")
    private Boolean attending;
}
