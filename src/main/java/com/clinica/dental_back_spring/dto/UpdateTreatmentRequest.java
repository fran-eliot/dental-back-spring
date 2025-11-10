package com.clinica.dental_back_spring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Petición de actualización de un tratamiento")
public class UpdateTreatmentRequest {

    @Schema(description="Nombre del tratamiento",example = "Limpieza dental")
    private String name;

    @Schema(description="Tipo de tratamiento",example = "Higiene")
    private String type;

    @Positive(message = "La duración debe ser un número positivo")
    @Schema(description="Duración en minutos del tratamiento", example = "30")
    private Integer duration;

    @Positive(message = "El precio debe ser positivo")
    @Schema(description="Precio del tratamiento en euros", example = "45.0")
    private Double price;

    @Schema(description = "Visible para pacientes", example = "true")
    private Boolean visible;
}

