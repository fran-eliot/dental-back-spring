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
@Schema(description = "Petición para crear un nuevo profesional")
public class CreateProfessionalRequest {

    @NotBlank(message = "El NIF es obligatorio")
    @Schema(description="NIF del profesional",example = "98765432B")
    private String nif;

    @NotBlank(message = "El número de licencia es obligatorio")
    @Schema(description="Número de licencia del profesional", example = "DENT-001")
    private String license;

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description="Nombre del profesional", example = "Laura")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    @Schema(description="Apellido del profesional", example = "Gómez")
    private String lastName;

    @Pattern(regexp = "^[0-9+()\\-\\s]*$", message = "Formato de teléfono no válido")
    @Schema(description="Teléfono del profesional", example = "600123456")
    private String phone;

    @Email(message = "Formato de email no válido")
    @Schema(description="Email del profesional", example = "laura@smyle.es")
    private String email;

    @NotBlank(message="Debe indicar la sala asignada al profesional")
    @Schema(description="Sala asignada al profesional", example = "Sala 2")
    private String room;

}

