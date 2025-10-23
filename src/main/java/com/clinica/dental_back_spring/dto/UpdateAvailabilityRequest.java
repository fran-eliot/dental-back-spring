package com.clinica.dental_back_spring.dto;

import com.clinica.dental_back_spring.enums.StatusAvailability;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateAvailabilityRequest {
    private Long professionalId;
    private LocalDate date;
    private StatusAvailability status;
    private Long slotId;
}

