package com.clinica.dental_back_spring.controller;

import com.clinica.dental_back_spring.dto.UserDTO;
import com.clinica.dental_back_spring.entity.User;
import com.clinica.dental_back_spring.enums.Role;
import com.clinica.dental_back_spring.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/users")
@Tag(name = "Usuarios", description = "Operaciones de gestión de usuarios (solo administrador)")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ==========================================================
    // 🔹 GET /users → Listar todos los usuarios
    // ==========================================================
    @Operation(summary = "Listar todos los usuarios")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> list = userRepository.findAll().stream()
                .map(u -> new UserDTO(u.getId(), u.getEmail(), u.getRole().name()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // ==========================================================
    // 🔹 GET /users/:id → Obtener un usuario por ID
    // ==========================================================
    @Operation(summary = "Obtener usuario por ID")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        var optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User u = optionalUser.get();
            UserDTO dto = new UserDTO(u.getId(), u.getEmail(), u.getRole().name());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "Usuario no encontrado"));
        }
    }

    // ==========================================================
    // 🔹 PATCH /users/:id/role → Cambiar rol
    // ==========================================================
    @Operation(summary = "Actualizar rol de usuario", description = "Permite cambiar el rol (admin, dentista o paciente).")
    @ApiResponse(responseCode = "200", description = "Rol actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    @PatchMapping("/{id}/role")
    public ResponseEntity<?> updateRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        String newRole = body.get("role");
        return userRepository.findById(id)
                .map(user -> {
                    try {
                        user.setRole(Role.valueOf(newRole.toUpperCase()));
                        userRepository.save(user);
                        return ResponseEntity.ok(Map.of("message", "Rol actualizado correctamente"));
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body(Map.of("message", "Rol no válido"));
                    }
                })
                .orElse(ResponseEntity.status(404).body(Map.of("message", "Usuario no encontrado")));
    }

    // ==========================================================
    // 🔹 PATCH /users/:id/password → Cambiar contraseña
    // ==========================================================
    @Operation(summary = "Actualizar contraseña de usuario")
    @ApiResponse(responseCode = "200", description = "Contraseña actualizada correctamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    @PatchMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        String newPassword = body.get("password");
        if (newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "La nueva contraseña no puede estar vacía"));
        }

        return userRepository.findById(id)
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    userRepository.save(user);
                    return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente"));
                })
                .orElse(ResponseEntity.status(404).body(Map.of("message", "Usuario no encontrado")));
    }

    // ==========================================================
    // 🔹 DELETE /users/:id → Desactivar usuario (soft delete)
    // ==========================================================
    @Operation(summary = "Desactivar usuario", description = "Marca un usuario como inactivo sin eliminarlo de la base de datos.")
    @ApiResponse(responseCode = "204", description = "Usuario desactivado correctamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setActive(false);
                    userRepository.save(user);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.status(404).body(Map.of("message", "Usuario no encontrado")));
    }
}
