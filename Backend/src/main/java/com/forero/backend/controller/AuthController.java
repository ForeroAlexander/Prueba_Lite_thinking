package com.forero.backend.controller;

import com.forero.backend.dto.AuthResponse;
import com.forero.backend.dto.LoginRequest;
import com.forero.backend.dto.SignUpRequest;
import com.forero.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            log.debug("Intento de login para el email: {}", loginRequest.getEmail());
            AuthResponse response = authService.login(loginRequest);
            log.debug("Login exitoso para el email: {}", loginRequest.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error en el endpoint de login: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error en la autenticación: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            log.debug("Intento de registro para el email: {}", signUpRequest.getEmail());
            AuthResponse response = authService.register(signUpRequest);
            log.debug("Registro exitoso para el email: {}", signUpRequest.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error en el endpoint de registro: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error en el registro: " + e.getMessage()));
        }
    }
}

@lombok.Data
@lombok.AllArgsConstructor
class ErrorResponse {
    private String error;
}
