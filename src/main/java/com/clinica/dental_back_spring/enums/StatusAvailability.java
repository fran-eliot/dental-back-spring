package com.clinica.dental_back_spring.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
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

    @JsonCreator
    public static StatusAvailability fromValue(String value) {
        for (StatusAvailability status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Estado de disponibilidad no válido: " + value);
    }
}

