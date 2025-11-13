package com.clinica.dental_back_spring.controller;

import com.clinica.dental_back_spring.dto.UserDTO;
import com.clinica.dental_back_spring.entity.User;
import com.clinica.dental_back_spring.enums.Role;
import com.clinica.dental_back_spring.repository.UserRepository;
import com.clinica.dental_back_spring.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
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

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ==========================================================
    // 🔹 GET /users → Listar todos los usuarios
    // ==========================================================
    @GetMapping
    @Operation(summary = "Listar todos los usuarios")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    // ==========================================================
    // 🔹 GET /users/:id → Obtener un usuario
    // ==========================================================
    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.findById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    // ==========================================================
    // 🔹 PATCH /users/:id/role → Cambiar rol
    // ==========================================================
    @PatchMapping("/{id}/role")
    @Operation(summary = "Actualizar rol de usuario")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestBody UpdateRoleRequest req) {

        if (req.getRole() == null || req.getRole().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "El rol no puede estar vacío"));
        }

        try {
            userService.updateRole(id, req.getRole());
            return ResponseEntity.ok(Map.of("message", "Rol actualizado correctamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // ==========================================================
    // 🔹 PATCH /users/:id/password → Cambiar contraseña
    // ==========================================================
    @PatchMapping("/{id}/password")
    @Operation(summary = "Actualizar contraseña de usuario")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @RequestBody UpdatePasswordRequest req) {
        try {
            userService.updatePassword(id, req.getPassword());
            return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // ==========================================================
    // 🔹 DELETE /users/:id → Soft delete
    // ==========================================================
    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar usuario")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
        try {
            userService.deactivate(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    // ==========================================================
    // DTOs internos
    // ==========================================================
    @Getter
    @Setter
    public static class UpdateRoleRequest {
        private String role;
    }

    @Getter @Setter
    public static class UpdatePasswordRequest {
        private String password;
    }
}

