package com.clinica.dental_back_spring.controller;

import com.clinica.dental_back_spring.enums.Role;
import com.clinica.dental_back_spring.entity.User;
import com.clinica.dental_back_spring.repository.UserRepository;
import com.clinica.dental_back_spring.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "Endpoints para registro, login y validación de usuario")
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ==========================================================
    // 🩺 Registro de nuevo paciente
    // ==========================================================
    @Operation(
            summary = "Registrar nuevo paciente",
            description = "Crea una cuenta de usuario con rol PACIENTE usando email y contraseña.",
            requestBody = @RequestBody(
                    required = true,
                    description = "Datos del nuevo usuario",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\"email\": \"paciente@correo.com\", \"password\": \"123456\"}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paciente creado correctamente",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"message\": \"created\"}"))),
                    @ApiResponse(responseCode = "400", description = "Email ya registrado",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"message\": \"Email already exists\"}")))
            }
    )
    @PostMapping("/register-patient")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        if (userRepo.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already exists"));
        }
        User u = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.PACIENTE)
                .active(true)
                .build();
        userRepo.save(u);
        return ResponseEntity.ok(Map.of("message", "created"));
    }

    // ==========================================================
    // 🔐 Login
    // ==========================================================
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica un usuario mediante email y contraseña. Devuelve un token JWT si las credenciales son válidas.",
            requestBody = @RequestBody(
                    required = true,
                    description = "Credenciales del usuario",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"email\": \"paciente@correo.com\", \"password\": \"123456\"}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login exitoso",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"token\": \"jwt-token-generado\"}"))),
                    @ApiResponse(responseCode = "401", description = "Credenciales inválidas",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"message\": \"invalid credentials\"}")))
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        String password = body.get("password");
        return userRepo.findByEmail(email).map(user -> {
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.status(401).body(Map.of("message", "invalid credentials"));
            }
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());
            return ResponseEntity.ok(Map.of("token", token));
        }).orElse(ResponseEntity.status(401).body(Map.of("message", "invalid credentials")));
    }

    // ==========================================================
    // 👤 Obtener datos del usuario autenticado
    // ==========================================================
    @Operation(
            summary = "Obtener información del usuario autenticado",
            description = "Devuelve la información contenida en el token JWT actual.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Datos del usuario extraídos del token",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"email\": \"paciente@correo.com\", \"role\": \"PACIENTE\", \"userId\": 1}"))),
                    @ApiResponse(responseCode = "401", description = "Token ausente o inválido")
            }
    )
    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        String token = authHeader.substring(7);
        var claims = jwtUtil.extractAllClaims(token);
        return ResponseEntity.ok(Map.of(
                "email", claims.getSubject(),
                "role", claims.get("role"),
                "userId", claims.get("userId")
        ));
    }
}

