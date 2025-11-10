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
@Schema(description = "Petición para registrar un usuario")
public class RegisterRequest {

    @Email(message = "Debe ser un correo válido")
    @NotBlank(message = "El email es obligatorio")
    @Schema(description="Email de registro",example="laura@smyle.com")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description="Contraseña de registro",example="123456")
    private String password;
}

