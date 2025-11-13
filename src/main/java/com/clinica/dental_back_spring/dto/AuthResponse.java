package com.clinica.dental_back_spring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta de autenticación exitosa")
public class AuthResponse {

    @Schema(description = "Token JWT generado tras la autenticación", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6...")
    private String token;

    @Schema(
            description = "Datos del usuario autenticado (id, email, rol)",
            example = "{ \"id\": 1, \"email\": \"admin@smyle.es\", \"role\": \"admin\" }"
    )
    private UserDTO user;

    @Schema(description = "ID del profesional asociado (solo si es dentista)")
    private Long professionalId;
}

