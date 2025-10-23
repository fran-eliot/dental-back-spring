package com.clinica.dental_back_spring.dto;

import lombok.Data;

@Data
public class UpdateProfessionalRequest {
    private String nif;
    private String licence;
    private String name;
    private String lastName;
    private String phone;
    private String email;
    private String room;
    private Boolean active;
}
