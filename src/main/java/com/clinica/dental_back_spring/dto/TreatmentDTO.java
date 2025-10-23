package com.clinica.dental_back_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentDTO {
    private Long id;
    private String name;
    private String type;
    private Integer duration;
    private double price;
    private boolean visible;
}
