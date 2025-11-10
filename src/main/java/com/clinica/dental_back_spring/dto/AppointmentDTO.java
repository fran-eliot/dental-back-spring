package com.clinica.dental_back_spring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de una cita")
public class AppointmentDTO {

    @Schema(description = "Identificador de la cita", example = "1")
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Fecha y hora de la cita", example = "2025-11-10T09:00:00")
    private LocalDateTime date;

    @Positive(message = "La duración debe ser un número positivo")
    @Schema(description = "Duración en minutos", example = "30")
    private Integer duration;

    @Schema(description = "Estado actual de la cita", example = "pendiente")
    private String status;

    @Schema(description = "Usuario que creó la cita (admin, profesional o paciente)", example = "admin")
    private String createdBy;

    @Schema(
            description = "Profesional asignado a la cita (datos simplificados)",
            example = "{ \"id\": 2, \"name\": \"Laura\", \"lastName\": \"Gómez\" }"
    )
    private SimpleProfessionalDTO professional;

    @Schema(
            description = "Paciente asociado a la cita (datos simplificados)",
            example = "{ \"id\": 5, \"firstName\": \"Lucía\", \"lastName\": \"Santos\" }"
    )
    private SimplePatientDTO patient;

    @Schema(
            description = "Tratamiento realizado en la cita (datos simplificados)",
            example = "{ \"id\": 1, \"name\": \"Limpieza dental\" }"
    )
    private SimpleTreatmentDTO treatment;
}
