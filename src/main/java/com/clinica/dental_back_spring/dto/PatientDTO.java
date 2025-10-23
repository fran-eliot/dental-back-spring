package com.clinica.dental_back_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private Long id;
    private String nif;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean active;
}

