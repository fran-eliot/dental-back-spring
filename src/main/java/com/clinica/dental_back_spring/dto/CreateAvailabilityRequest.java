package com.clinica.dental_back_spring.dto;

import com.clinica.dental_back_spring.enums.StatusAvailability;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateAvailabilityRequest {
    @NotNull
    private Long professionalId;

    @NotNull
    private LocalDate date;

    @NotNull
    private StatusAvailability status;

    // opcional
    private Long slotId;
}

