package com.clinica.dental_back_spring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateProfessionalRequest {
    @NotBlank
    private String nif;

    @NotBlank
    private String licence;

    @NotBlank
    private String name;

    private String lastName;
    private String phone;
    private String email;
    private String room;
}

