package com.clinica.dental_back_spring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Petición para crear una nueva cita en el sistema")
public class CreateAppointmentRequest {

    @NotNull(message="El ID del slot es obligatorio")
    @Schema(description="Identificador del slot de tiempo", example = "3")
    private Long slotId;

    @NotNull(message="El ID del paciente es obligatorio")
    @Schema(description="Identificador del paciente", example = "5")
    private Long patientId;

    @NotNull(message="El ID del profesional es obligatorio")
    @Schema(description="Identificador del profesional (dentista)", example = "2")
    private Long professionalId;

    @NotNull(message="El ID del tratamiento es obligatorio")
    @Schema(description="Identificador del tratamiento", example = "1")
    private Long treatmentId;

    @Schema(
            description = "Usuario que crea la cita (admin o professional)",
            example = "admin"
    )
    private String createdBy;
}
