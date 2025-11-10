package com.clinica.dental_back_spring.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AppointmentStatus {
    PENDIENTE("pendiente"),
    CONFIRMADA("confirmada"),
    CANCELADA("cancelada"),
    REALIZADA("realizada");

    private final String value;

    AppointmentStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static AppointmentStatus fromString(String status) {
        for (AppointmentStatus s : values()) {
            if (s.value.equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Estado no válido: " + status);
    }
}


