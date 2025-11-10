package com.clinica.dental_back_spring.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Petición para actualizar el estado de una cita")
public class UpdateAppointmentRequest {
    @NotBlank(message = "Debe especificar el estado de la cita")
    @Schema(description="Estado de la cita",example = "confirmada")
    private String status;
}

