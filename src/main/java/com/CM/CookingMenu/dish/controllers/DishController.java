package com.CM.CookingMenu.dish.controllers;

import com.CM.CookingMenu.dish.ai.dtos.RecipeSuggestionRequestDTO;
import com.CM.CookingMenu.dish.ai.dtos.RecipeSuggestionResponseDTO;
import com.CM.CookingMenu.dish.ai.services.AIRecipeService;
import com.CM.CookingMenu.dish.dtos.DishDTO;
import com.CM.CookingMenu.dish.services.DishService;
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

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/dishes")
@Validated
@Tag(name = "Dish", description = "Dish Management API")
@SecurityRequirement(name = "Bearer Authentication")
public class DishController {
    private final DishService dishService;
    private final AIRecipeService aiRecipeService;

    @Operation(
            summary = "Get all dishes",
            description = "Retrieves a list of all the dishes in the system."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of dishes.",
                    content = @Content(schema = @Schema(implementation = DishDTO.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role.")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'COOK', 'ADMIN')")
    public ResponseEntity<List<DishDTO>> getAllDishes() {
        return ResponseEntity.ok(dishService.getAllDishes());
    }

    @Operation(
            summary = "Add new dish",
            description = "Creates a new dish in the system. Requires COOK or ADMIN role."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Dish created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input or dish name already exists."),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role.")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<String> saveDish(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dish to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = DishDTO.class))
            )
            @Valid @RequestBody DishDTO dto) {
        dishService.saveDish(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Dish Added.");
    }

    @Operation(
            summary = "Delete a dish by name",
            description = "Deletes a dish from the system. Cannot delete if it's being used in any menus. Requires COOK or ADMIN role."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dish deleted successfully."),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role."),
            @ApiResponse(responseCode = "404", description = "Not found - Ingredient not found."),
            @ApiResponse(responseCode = "409", description = "Conflict - Ingredient is being used in a menu")
    })
    @DeleteMapping("/delete/{name}")
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<String> deleteDish(@PathVariable
                                             @Parameter(
                                                     description = "Name of dish to delete",
                                                     example = "French Fries",
                                                     required = true
                                             )
                                             @Pattern(regexp = "^[a-zA-Z_\\s]{2,100}$", message = "Invalid Dish name format")
                                             @NotBlank(message = "Dish name cannot be blank.") String name) {
        if (name == null || name.trim().isBlank())
            throw new IllegalArgumentException("Dish name cannot be null or blank.");

        dishService.deleteDishByName(name);

        return ResponseEntity.ok("Dish deleted successfully.");
    }

    @Operation(
            summary = "Update a dish",
            description = "Update dish properties in the system. Requires COOK or ADMIN role."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dish updated successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role."),
            @ApiResponse(responseCode = "404", description = "Not found - Dish not found.")
    })
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<String> updateDish(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dish to update",
                    required = true,
                    content = @Content(schema = @Schema(implementation = DishDTO.class))
            )
            @Valid @RequestBody DishDTO dishDTO) {
        dishService.updateDish(dishDTO);
        return ResponseEntity.ok("Dish updated successfully.");
    }

    @Operation(
            summary = "Suggest a dish by AI",
            description = "AI Suggestion for a dish according to available ingredients. Requires COOK or ADMIN role."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Suggestion generated successfully.",
                    content = @Content(schema = @Schema(implementation = RecipeSuggestionResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role.")
    })
    @PostMapping("/suggestions")
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<List<RecipeSuggestionResponseDTO>> getRecipeSuggestions(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Recipe suggestion request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RecipeSuggestionRequestDTO.class))
            )
            @Valid @RequestBody RecipeSuggestionRequestDTO request) {
        List<RecipeSuggestionResponseDTO> suggestions = aiRecipeService.generateRecipeSuggestions(request);
        return ResponseEntity.ok(suggestions);
    }
}
