package com.CM.CookingMenu.foodmenu.controllers;

import com.CM.CookingMenu.common.utils.DateUtils;
import com.CM.CookingMenu.foodmenu.ai.dtos.FoodMenuSuggestionRequestDTO;
import com.CM.CookingMenu.foodmenu.ai.dtos.FoodMenuSuggestionResponseDTO;
import com.CM.CookingMenu.foodmenu.ai.services.FoodMenuSuggestionService;
import com.CM.CookingMenu.foodmenu.dtos.AttendanceRequestDTO;
import com.CM.CookingMenu.foodmenu.dtos.FoodMenuDTO;
import com.CM.CookingMenu.foodmenu.services.FoodMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/menus")
@Validated
@Tag(name = "Food Menu", description = "Food Menu Management API")
@SecurityRequirement(name = "Bearer Authentication")
public class FoodMenuController {
    private final FoodMenuService foodMenuService;
    private final FoodMenuSuggestionService foodMenuSuggestionService;

    @Operation(
            summary = "Get all future food menus",
            description = "Retrieves a list of future food menus"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of Food Menus",
                    content = @Content(schema = @Schema(implementation = FoodMenuDTO.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token."),
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'COOK', 'ADMIN')")
    public ResponseEntity<List<FoodMenuDTO>> getAllFutureMenus() {
        return ResponseEntity.ok(foodMenuService.getAllFutureMenus());
    }

    @Operation(
            summary = "Get all food menus",
            description = "Retrieves a list of all food menus. Requires COOK or ADMIN role."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of Food Menus.",
                    content = @Content(schema = @Schema(implementation = FoodMenuDTO.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role."),
    })
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<List<FoodMenuDTO>> getAllFoodMenus() {
        return ResponseEntity.ok(foodMenuService.getAllFoodMenus());
    }

    @Operation(
            summary = "Get all food menus containing a dish",
            description = "Retrieves a list of all food menus containing a dish. Requires COOK or ADMIN role."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of Food Menus containing a dish.",
                    content = @Content(schema = @Schema(implementation = FoodMenuDTO.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role."),
            @ApiResponse(responseCode = "404", description = "Not found - Dish not found.")
    })
    @GetMapping("/{dishName}")
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<List<FoodMenuDTO>> getAllFoodMenusContainingDish(@PathVariable
                                                                           @Parameter(
                                                                                   description = "Name of dish to search by",
                                                                                   example = "French fries",
                                                                                   required = true
                                                                           )
                                                                           @Pattern(regexp = "^[a-zA-Z\\s]{2,100}$", message = "Invalid dish name format")
                                                                           @NotBlank(message = "Dish name cannot be blank.")
                                                                           String dishName) {
        if (dishName == null || dishName.trim().isBlank())
            throw new IllegalArgumentException("Dish name cannot be null or empty");
        return ResponseEntity.ok(foodMenuService.getAllFoodMenusContainingDish(dishName.trim()));
    }

    @Operation(
            summary = "Add new Food Menu",
            description = "Adds a new Food Menu in the system. Requires COOK or ADMIN role."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Food Menu added successfully."),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input or Food Menu date already exists."),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role.")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<String> saveFoodmenu(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Food Menu to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = FoodMenuDTO.class))
            )
            @Valid @RequestBody FoodMenuDTO dto) {
        foodMenuService.saveFoodmenu(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Foodmenu added.");
    }

    @Operation(
            summary = "Delete a Food Menu by date",
            description = "Deletes a Food Menu by date in the system. Requires COOK or ADMIN role."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Food Menu deleted successfully."),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role."),
            @ApiResponse(responseCode = "404", description = "Not Found - Food Menu date not found ")
    })
    @DeleteMapping("/delete/{menuDate}")
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<String> deleteMenu(@PathVariable
                                             @Parameter(
                                                     description = "Menu Date to delete",
                                                     required = true,
                                                     example = "2025-10-25"
                                             )
                                             @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format. Use YYYY-MM-DD")
                                             @NotBlank(message = "Date cannot be blank.") String menuDate) {
        LocalDate date = DateUtils.parseStringToLocalDate(menuDate);

        foodMenuService.deleteFoodmenuByDate(date);

        return ResponseEntity.ok("Foodmenu deleted successfully.");
    }

    @Operation(
            summary = "Update a Food Menu",
            description = "Update Food Menu properties in the system. Requires COOK or ADMIN role."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Food Menu updated successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role."),
            @ApiResponse(responseCode = "404", description = "Not found - Food Menu date not found.")
    })
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<String> updateFoodmenu(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Food Menu to update",
                    required = true,
                    content = @Content(schema = @Schema(implementation = FoodMenuDTO.class))
            )
            @RequestBody @Valid FoodMenuDTO foodmenuDTO) {
        foodMenuService.updateFoodmenu(foodmenuDTO);
        return ResponseEntity.ok("Successfully updated the foodmenu.");
    }

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
    @PostMapping("/attend")
    @PreAuthorize("hasAnyRole('USER', 'COOK', 'ADMIN')")
    public ResponseEntity<String> attendMenu(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Food Menu to attend",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AttendanceRequestDTO.class))
            )
            @Valid @RequestBody AttendanceRequestDTO dto) {
        String message = foodMenuService.updateAttendance(dto);
        return ResponseEntity.ok(message);
    }

    @Operation(
            summary = "Generate a week of Food Menus",
            description = "Generate Food Menus for a week with AI. Requires COOK or ADMIN role."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Weekly Food Menu generated successfully.",
                    content = @Content(schema = @Schema(implementation = FoodMenuSuggestionResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role.")
    })
    @PostMapping("/generate")
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<FoodMenuSuggestionResponseDTO> getFoodMenuSuggestions(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Food Menu Suggestion Request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = FoodMenuSuggestionRequestDTO.class))
            )
            @Valid @RequestBody FoodMenuSuggestionRequestDTO dto) {
        FoodMenuSuggestionResponseDTO suggestions = foodMenuSuggestionService.generateFoodMenuSuggestion(dto);
        return ResponseEntity.ok(suggestions);
    }
}
