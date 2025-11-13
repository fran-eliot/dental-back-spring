package com.clinica.dental_back_spring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de un profesional")
public class ProfessionalDTO {

    @Schema(description = "Identificador del profesional (dentista)", example = "1")
    private Long id;

    @Schema(description="NIF del profesional", example = "98765432B")
    private String nif;

    @Schema(description="Número de licencia del profesional",example = "DENT-001")
    private String license;

    @Schema(description="Nombre del profesional", example = "Laura")
    private String name;

    @Schema(description="Apellido del profesional", example = "Gómez")
    private String lastName;

    @Pattern(regexp = "^[0-9+()\\-\\s]*$", message = "Formato de teléfono no válido")
    @Schema(description="Teléfono del profesional",example = "600123456")
    private String phone;

    @Email(message = "Debe ser un email válido")
    @Schema(description="Email del profesional", example = "laura@smyle.es")
    private String email;

    @Schema(description="Sala asignada al profesional", example = "Sala 2")
    private String room;

    @Schema(description="Indica si el profesional está activo",example = "true")
    private boolean active;

    @Schema(description = "Identificador usuario", example = "5")
    private Long userId;
}

