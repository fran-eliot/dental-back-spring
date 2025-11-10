package com.clinica.dental_back_spring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Petición de actualización de un profesional")
public class UpdateProfessionalRequest {

    @Schema(description="NIF del profesional", example = "98765432B")
    private String nif;

    @Schema(description="Número de licencia del profesional",example = "DENT-001")
    private String licence;

    @Schema(description="Nombre del profesional", example = "Laura")
    private String name;

    @Schema(description="Apellido del profesional", example = "Gómez")
    private String lastName;

    @Pattern(regexp = "^[0-9+()\\-\\s]*$", message = "Formato de teléfono no válido")
    @Schema(description="Teléfono del profesional",example = "600123456")
    private String phone;

    @Email(message = "Formato de email no válido")
    @Schema(description="Email del profesional", example = "laura@smyle.es")
    private String email;

    @Schema(description="Sala asignada al profesional", example = "Sala 2")
    private String room;

    @Schema(description="Indica si el profesional está activo",example = "true")
    private Boolean active;
}
