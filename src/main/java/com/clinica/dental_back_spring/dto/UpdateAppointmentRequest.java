package com.clinica.dental_back_spring.dto;


import com.clinica.dental_back_spring.enums.AppointmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Petición para actualizar el estado de una cita")
public class UpdateAppointmentRequest {
    @NotNull(message = "El estado no puede estar vacío")
    @Schema(
            description = "Nuevo estado de la cita",
            example = "CONFIRMADA",
            allowableValues = {"PENDIENTE", "CONFIRMADA", "REALIZADA", "CANCELADA"}
    )
    private AppointmentStatus status;
}

