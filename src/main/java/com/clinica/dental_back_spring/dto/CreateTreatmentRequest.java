package com.clinica.dental_back_spring.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTreatmentRequest {
    @NotBlank
    private String name;

    private String type;

    @Min(1)
    private Integer duration;

    private double price;

    private Boolean visible;
}

