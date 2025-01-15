package com.forero.backend.service;

import com.forero.backend.dto.AuthResponse;
import com.forero.backend.dto.LoginRequest;
import com.forero.backend.dto.SignUpRequest;
import com.forero.backend.model.Usuario;
import com.forero.backend.repository.UsuarioRepository;
import com.forero.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(SignUpRequest request) {
        try {
            if (usuarioRepository.existsByEmail(request.getEmail())) {
                log.warn("Intento de registro con email ya existente: {}", request.getEmail());
                throw new RuntimeException("El email ya está registrado");
            }

            String normalizedRole = request.getRole().trim().toLowerCase();
            if (!normalizedRole.equals("admin") && !normalizedRole.equals("external")) {
                log.warn("Intento de registro con rol inválido: {}", request.getRole());
                throw new RuntimeException("Rol inválido");
            }

            Usuario usuario = new Usuario();
            usuario.setEmail(request.getEmail());
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
            usuario.setRole(normalizedRole);

            usuario = usuarioRepository.save(usuario);
            log.debug("Usuario guardado exitosamente: {}", usuario.getEmail());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            String token = tokenProvider.generateToken(authentication);
            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            return new AuthResponse(token, usuario.getId().toString(), usuario.getEmail(), role);
        } catch (Exception e) {
            log.error("Error en el proceso de registro: {}", e.getMessage(), e);
            throw new RuntimeException("Error en el proceso de registro: " + e.getMessage());
        }
    }

    public AuthResponse login(LoginRequest request) {
        try {
            log.debug("Intentando autenticar usuario: {}", request.getEmail());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            log.debug("Usuario autenticado exitosamente: {}", request.getEmail());

            String token = tokenProvider.generateToken(authentication);
            log.debug("Token JWT generado exitosamente");

            Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(auth -> auth.replace("ROLE_", ""))
                    .collect(Collectors.joining(","));

            return new AuthResponse(token, usuario.getId().toString(), request.getEmail(), role);
        } catch (Exception e) {
            log.error("Error en el proceso de login: {}", e.getMessage(), e);
            throw new RuntimeException("Error en el proceso de autenticación: " + e.getMessage());
        }
    }
}
