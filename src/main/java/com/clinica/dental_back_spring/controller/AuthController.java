package com.clinica.dental_back_spring.controller;

import com.clinica.dental_back_spring.entity.User;
import com.clinica.dental_back_spring.enums.Role;
import com.clinica.dental_back_spring.repository.UserRepository;
import com.clinica.dental_back_spring.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Registro, inicio de sesión y datos del usuario autenticado")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ==========================================================
    // 🔹 POST /auth/register-patient
    // ==========================================================
    @Operation(summary = "Registrar nuevo paciente")
    @ApiResponse(responseCode = "200", description = "Paciente registrado correctamente")
    @ApiResponse(responseCode = "400", description = "El email ya está registrado", content = @Content)
    @PostMapping("/register-patient")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already exists"));
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.ROLE_PACIENTE)
                .active(true)
                .build();

        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "created"));
    }

    // ==========================================================
    // 🔹 POST /auth/login
    // ==========================================================
    @Operation(summary = "Iniciar sesión")
    @ApiResponse(responseCode = "200", description = "Inicio de sesión correcto", content = @Content(
            schema = @Schema(example = "{\"token\": \"jwt_token_aquí\"}")
    ))
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content)
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        return userRepository.findByEmail(email)
                .map(user -> {
                    if (!passwordEncoder.matches(password, user.getPassword())) {
                        return ResponseEntity.status(401).body(Map.of("message", "invalid credentials"));
                    }

                    String token = jwtUtil.generateToken(
                            user.getEmail(),
                            user.getRole().name(),
                            user.getId()
                    );

                    return ResponseEntity.ok(Map.of("token", token));
                })
                .orElse(ResponseEntity.status(401).body(Map.of("message", "invalid credentials")));
    }

    // ==========================================================
    // 🔹 GET /auth/me
    // ==========================================================
    @Operation(summary = "Obtener datos del usuario autenticado")
    @ApiResponse(responseCode = "200", description = "Datos obtenidos correctamente")
    @ApiResponse(responseCode = "401", description = "Token inválido o ausente", content = @Content)
    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("message", "missing or invalid token"));
        }

        String token = authHeader.substring(7);

        try {
            var claims = jwtUtil.extractAllClaims(token);
            return ResponseEntity.ok(Map.of(
                    "email", claims.getSubject(),
                    "role", claims.get("role"),
                    "userId", claims.get("userId")
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("message", "invalid token"));
        }
    }

    // ==========================================================
    // 🔹 DTOs internos
    // ==========================================================
    public static class RegisterRequest {
        @Email(message = "Debe ser un email válido")
        @NotBlank(message = "El email es obligatorio")
        private String email;

        @NotBlank(message = "La contraseña es obligatoria")
        private String password;

        // Getters y setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginRequest {
        @Email(message = "Debe ser un email válido")
        @NotBlank(message = "El email es obligatorio")
        private String email;

        @NotBlank(message = "La contraseña es obligatoria")
        private String password;

        // Getters y setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}


