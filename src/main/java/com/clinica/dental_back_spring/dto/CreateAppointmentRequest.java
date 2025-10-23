package com.clinica.dental_back_spring.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAppointmentRequest {

    @NotNull
    private Long slotId;

    @NotNull
    private Long patientId;

    @NotNull
    private Long professionalId;

    @NotNull
    private Long treatmentId;

    // opcional: createdBy si lo quieres controlar desde frontend, sino lo inferes del token
    private String createdBy;
}
