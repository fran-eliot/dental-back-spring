package com.clinica.dental_back_spring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Petición para crear un nuevo tratamiento")
public class CreateTreatmentRequest {
    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description="Nombre del tratamiento",example = "Limpieza dental")
    private String name;

    @NotBlank(message = "El tipo de tratamiento es obligatorio")
    @Schema(description="Tipo de tratamiento", example = "Higiene")
    private String type;

    @Positive(message = "La duración debe ser un número positivo")
    @Schema(description="Duración en minutos", example = "30")
    private Integer duration;

    @Positive(message = "El precio debe ser positivo")
    @Schema(description="Precio del tratamiento en euros", example = "45.0")
    private BigDecimal price;

    @NotBlank(message="Debe indicar si el tratamiento está visible")
    @Schema(description = "Visibile para pacientes", example = "true")
    private Boolean visible;
}

