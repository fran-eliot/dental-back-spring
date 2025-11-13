package com.clinica.dental_back_spring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description="Datos del usuario")
public class UserDTO {

    @NotNull(message="El ID del usuario es obligatorio")
    @Schema(description="Id del usuario",example = "3")
    private Long id;

    @Email(message = "Debe ser un correo electrónico válido")
    @NotBlank(message = "El email es obligatorio")
    @Schema(description="Email del usuario",example="admin@smyle.com")
    private String email;

    @NotBlank(message="El rol del usuario es obligatorio")
    @Schema(description="Rol del usuario",example="admin")
    private String role;

    @Schema(description="Indica si el usuario está activo",example="true")
    private boolean active;
}

