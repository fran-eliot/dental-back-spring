package com.clinica.dental_back_spring.dto;

import com.clinica.dental_back_spring.enums.StatusAvailability;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityDTO {
    private Long id;
    private Long professionalId;
    private LocalDate date;
    private StatusAvailability status;
    private Long slotId; // opcional, puede ser null
}

