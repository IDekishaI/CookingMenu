package com.CM.CookingMenu.auth.services;

import com.CM.CookingMenu.auth.dtos.AuthResponse;
import com.CM.CookingMenu.auth.dtos.LoginRequest;
import com.CM.CookingMenu.auth.dtos.RegisterRequest;
import com.CM.CookingMenu.auth.entities.Role;
import com.CM.CookingMenu.auth.entities.User;
import com.CM.CookingMenu.auth.exceptions.EmailAlreadyExistsException;
import com.CM.CookingMenu.auth.exceptions.InvalidCredentialsException;
import com.CM.CookingMenu.auth.exceptions.UsernameAlreadyExistsException;
import com.CM.CookingMenu.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepo.existsByUsername(request.username()))
            throw new UsernameAlreadyExistsException();
        if (userRepo.existsByEmail(request.email()))
            throw new EmailAlreadyExistsException();

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(Role.USER)
                .build();

        userRepo.save(user);

        String token = jwtService.generateToken(user);

        return new AuthResponse(token, user.getUsername(), user.getRole().name(), 3600000L);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.usernameOrEmail(), loginRequest.password()));
        User user = userRepo.findByUsernameOrEmail(loginRequest.usernameOrEmail(), loginRequest.usernameOrEmail())
                .orElseThrow(() -> new InvalidCredentialsException());

        String token = jwtService.generateToken(user);

        return new AuthResponse(token, user.getUsername(), user.getRole().name(), 3600000L);
    }
}
