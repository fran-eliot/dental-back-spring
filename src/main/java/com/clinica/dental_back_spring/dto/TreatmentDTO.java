package com.clinica.dental_back_spring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de un tratamiento")
public class TreatmentDTO {

    @Schema(description = "ID del tratamiento", example = "1")
    private Long id;

    @Schema(description="Nombre del tratamiento",example = "Limpieza dental")
    private String name;

    @Schema(description="Tipo de tratamiento",example = "Higiene")
    private String type;

    @Positive(message = "La duración debe ser un número positivo")
    @Schema(description="Duración en minutos del tratamiento",example = "30")
    private Integer duration;

    @Positive(message = "El precio debe ser positivo")
    @Schema(description="Precio del tramiento en euros", example = "45.0")
    private BigDecimal price;;

    @Schema(description = "Visible para pacientes", example = "true")
    private boolean visible;
}
