package com.clinica.dental_back_spring.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdatePatientRequest {
    private String nif;
    private String firstName;
    private String lastName;

    @Email
    private String email;

    private String phone;
    private Boolean active;
}

