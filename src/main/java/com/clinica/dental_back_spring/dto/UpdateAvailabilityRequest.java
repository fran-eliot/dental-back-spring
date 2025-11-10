package com.clinica.dental_back_spring.dto;

import com.clinica.dental_back_spring.enums.StatusAvailability;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description="Petición de actualización de una disponibilidad")
public class UpdateAvailabilityRequest {

    @Schema(description="ID del profesional", example = "3")
    private Long professionalId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha de la disponibilidad", example = "2025-11-10")
    private LocalDate date;

    @Schema(description = "Estado de la disponibilidad", example = "libre")
    private String status;

    @Schema(description="ID del slot", example = "3")
    private Long slotId;
}

