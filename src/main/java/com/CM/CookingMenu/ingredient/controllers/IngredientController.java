package com.CM.CookingMenu.ingredient.controllers;

import com.CM.CookingMenu.ingredient.dtos.IngredientDTO;
import com.CM.CookingMenu.ingredient.services.IngredientService;
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
@RequestMapping("/ingredients")
@Validated
@Tag(name = "Ingredients", description = "Ingredient Management API")
@SecurityRequirement(name = "Bearer Authentication")
public class IngredientController {
    private final IngredientService ingredientService;

    @Operation(
            summary = "Get all ingredients",
            description = "Retrieves a list of all the usable ingredients in the system. Requires COOK or ADMIN role."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of ingredients.",
                    content = @Content(schema = @Schema(implementation = IngredientDTO.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role.")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<List<IngredientDTO>> getAllIngredients(){
        return ResponseEntity.ok(ingredientService.getAllIngredients());
    }

    @Operation(
            summary = "Add new ingredient",
            description = "Creates a new ingredient in the system. Requires COOK or ADMIN role."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ingredient created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input or ingredient already exists."),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role.")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<String> addIngredient(
                                                @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                        description = "Ingredient to create",
                                                        required = true,
                                                        content = @Content(schema = @Schema(implementation = IngredientDTO.class))
                                                )
                                                @Valid @RequestBody IngredientDTO ingredientDTO){
        ingredientService.addIngredient(ingredientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Ingredient Added.");
    }

    @Operation(
            summary = "Delete an ingredient by name",
            description = "Deletes an ingredient from the system. Cannot delete if it's being used in any dish. Requires COOK or ADMIN role."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ingredient deleted successfully."),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role."),
            @ApiResponse(responseCode = "404", description = "Not found - Ingredient not found."),
            @ApiResponse(responseCode = "409", description = "Conflict - Ingredient is being used in a dish")
    })
    @DeleteMapping("/delete/{name}")
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<String> deleteIngredient(@PathVariable
                                                   @Pattern(
                                                           regexp = "^[a-zA-Z_\\s]{2,100}$",
                                                           message = "Invalid ingredient name format"
                                                   )
                                                   @NotBlank(message = "Ingredient name cannot be blank.")
                                                   @Parameter(
                                                           description = "Name of ingredient to delete",
                                                           example = "Tomato",
                                                           required = true
                                                   )
                                                   String name){
        if(name == null || name.trim().isBlank())
            throw new IllegalArgumentException("Ingredient name cannot be null or blank");
        ingredientService.deleteIngredientByName(name);
        return ResponseEntity.ok("Ingredient deleted successfully");
    }

    @Operation(
            summary = "Update ingredient",
            description = "Updates ingredients properties in the system. Requires COOK or ADMIN role."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ingredient updated successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role."),
            @ApiResponse(responseCode = "404", description = "Not found - Ingredient not found.")
    })
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('COOK', 'ADMIN')")
    public ResponseEntity<String> updateIngredient(
                                                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                            description = "Ingredient with updated properties",
                                                            required = true,
                                                            content = @Content(schema = @Schema(implementation = IngredientDTO.class))
                                                    )
                                                    @Valid @RequestBody IngredientDTO ingredientDTO){
        ingredientService.updateIngredient(ingredientDTO);
        return ResponseEntity.ok("Ingredient updated successfully.");
    }
}
