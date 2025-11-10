package com.clinica.dental_back_spring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Petición para crear un nuevo paciente")
public class CreatePatientRequest {
    @NotBlank(message = "El NIF es obligatorio")
    @Schema(description="NIF del paciente",example = "12345678A")
    private String nif;

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description="Nombre del paciente",example = "Lucía")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Schema(description="Apellido del paciente", example = "Santos")
    private String lastName;

    @Email(message = "Formato de correo no válido")
    @Schema(description="Email del pacinete", example = "lucia@correo.com")
    private String email;

    @Pattern(regexp = "^[0-9+()\\-\\s]*$", message = "Formato de teléfono no válido")
    @Schema(description="Teléfono del paciente",example = "600123456")
    private String phone;

    @NotNull(message="Debe indicar si el paciente está activo")
    @Schema(description = "Indica si el Paciente está activo", example = "true")
    private boolean active;
}
