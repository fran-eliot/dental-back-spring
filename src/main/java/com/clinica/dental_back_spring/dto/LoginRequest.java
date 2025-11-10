package com.clinica.dental_back_spring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Credenciales de inicio de sesión")
public class LoginRequest {

    @Email(message = "Debe ser un correo válido")
    @NotBlank(message = "El email es obligatorio")
    @Schema(description="Email del login", example = "admin@smyle.es")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description="Contraseña del login",example = "123456")
    private String password;
}

