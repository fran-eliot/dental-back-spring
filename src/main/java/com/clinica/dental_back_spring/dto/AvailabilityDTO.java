package com.clinica.dental_back_spring.dto;

import com.clinica.dental_back_spring.enums.StatusAvailability;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Disponibilidad del profesional")
public class AvailabilityDTO {

    @Schema(description = "ID de la disponibilidad", example = "1")
    private Long id;

    @Schema(description="ID del profesional",example = "3")
    private Long professionalId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha de la disponibilidad", example = "2025-11-10")
    private LocalDate date;

    @Schema(description = "Estado actual de la disponibilidad", example = "libre")
    private StatusAvailability status;

    @Schema(description="ID del slot", example = "3")
    private Long slotId;
}

