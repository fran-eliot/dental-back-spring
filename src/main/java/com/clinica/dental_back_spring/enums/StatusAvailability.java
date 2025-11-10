package com.clinica.dental_back_spring.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusAvailability {
    LIBRE("libre"),
    RESERVADO("reservado"),
    NO_DISPONIBLE("no disponible");

    private final String value;

    StatusAvailability(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static StatusAvailability fromString(String status) {
        for (StatusAvailability s : values()) {
            if (s.value.equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Estado no válido: " + status);
    }
}

