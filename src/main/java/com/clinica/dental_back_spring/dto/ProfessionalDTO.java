package com.clinica.dental_back_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalDTO {
    private Long id;
    private String nif;
    private String licence;
    private String name;
    private String lastName;
    private String phone;
    private String email;
    private String room;
    private boolean active;
}

