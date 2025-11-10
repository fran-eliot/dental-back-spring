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
@Schema(description = "Datos de un paciente")
public class PatientDTO {

    @Schema(description = "ID del paciente", example = "1")
    private Long id;

    @Schema(description="NIF del pacinete", example = "12345678A")
    private String nif;

    @Schema(description = "Nombre del paciente", example = "Laura Gómez")
    private String firstName;

    @Schema(description="Apellido del paciente",example = "Santos")
    private String lastName;

    @Email(message = "Formato de email no válido")
    @Schema(description="Email del paciente", example = "lucia@correo.com")
    private String email;

    @Pattern(regexp = "^[0-9+()\\-\\s]*$", message = "Formato de teléfono no válido")
    @Schema(description="Teléfono del paciente", example = "600123456")
    private String phone;

    @Schema(description = "Indica si el Paciente está activo", example = "true")
    private boolean active;
}

