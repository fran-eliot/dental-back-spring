package com.clinica.dental_back_spring.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    ADMIN("admin"),
    DENTISTA("dentista"),
    PACIENTE("paciente");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Role fromValue(String value) {
        for (Role role : Role.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Rol desconocido: " + value);
    }
}


