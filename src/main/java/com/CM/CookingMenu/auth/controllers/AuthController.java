package com.CM.CookingMenu.auth.controllers;

import com.CM.CookingMenu.auth.dtos.AuthResponse;
import com.CM.CookingMenu.auth.dtos.LoginRequest;
import com.CM.CookingMenu.auth.dtos.RegisterRequest;
import com.CM.CookingMenu.auth.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "User registration and login endpoints")
public class AuthController {
    private final AuthService authService;
    @Operation(
            summary = "Register new user.",
            description = "Creates a new user account with the User role by default."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User registered successfully.",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Conflict - Username or email already exist.")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
                                                @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                        description = "User registration details",
                                                        required = true,
                                                        content = @Content(schema = @Schema(implementation = RegisterRequest.class))
                                                )
                                                @Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(
            summary = "Login user.",
            description = "Authenticates user and returns JWT Token valid for 1 hour."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User authenticated successfully.",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials.")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
                                            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                    description = "Login credentials",
                                                    required = true,
                                                    content = @Content(schema = @Schema(implementation = LoginRequest.class))
                                            )
                                            @Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }
}
