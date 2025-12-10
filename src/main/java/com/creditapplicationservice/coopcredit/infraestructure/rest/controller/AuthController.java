package com.creditapplicationservice.coopcredit.infraestructure.rest.controller;

import com.creditapplicationservice.coopcredit.infraestructure.rest.dto.AuthRequest;
import com.creditapplicationservice.coopcredit.infraestructure.rest.dto.AuthRegisterRequest;
import com.creditapplicationservice.coopcredit.infraestructure.rest.dto.AuthResponse;
import com.creditapplicationservice.coopcredit.infraestructure.security.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // -----------------------------
    // Register a new user
    // -----------------------------
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    // -----------------------------
    // Login and generate JWT
    // -----------------------------
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}

