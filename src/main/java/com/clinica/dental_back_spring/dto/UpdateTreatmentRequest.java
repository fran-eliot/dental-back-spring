package com.clinica.dental_back_spring.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateTreatmentRequest {
    private String name;
    private String type;

    @Min(1)
    private Integer duration;
    private Double price;
    private Boolean visible;
}

