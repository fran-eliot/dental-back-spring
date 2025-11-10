package com.clinica.dental_back_spring.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CreatedBy {
    ADMIN("admin"),
    PROFESSIONAL("professional"),
    PATIENT("patient");

    private final String value;

    CreatedBy(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}

