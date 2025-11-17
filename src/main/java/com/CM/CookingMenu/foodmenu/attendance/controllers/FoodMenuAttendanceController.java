package com.CM.CookingMenu.foodmenu.attendance.controllers;

import com.CM.CookingMenu.foodmenu.attendance.dtos.AttendanceRequestDTO;
import com.CM.CookingMenu.foodmenu.attendance.services.FoodMenuAttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/attend")
@Validated
@Tag(name = "Food Menu Attendance", description = "Food Menu Attendance Management API")
@SecurityRequirement(name = "Bearer Authentication")
public class FoodMenuAttendanceController {
    private final FoodMenuAttendanceService foodMenuAttendanceService;

    @Operation(
            summary = "Update attendance for a Food Menu",
            description = "Update attendance for a Food Menu."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated attendance successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role."),
            @ApiResponse(responseCode = "404", description = "Not found - Food Menu date not found.")
    })
    @PostMapping()
    @PreAuthorize("hasAnyRole('USER', 'COOK', 'ADMIN')")
    public ResponseEntity<String> attendMenu(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Food Menu to attend",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AttendanceRequestDTO.class))
            )
            @Valid @RequestBody AttendanceRequestDTO dto) {
        String message = foodMenuAttendanceService.updateAttendance(dto);
        return ResponseEntity.ok(message);
    }
}
