package com.clinica.dental_back_spring.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    ROLE_ADMIN("admin"),
    ROLE_DENTISTA("dentista"),
    ROLE_PACIENTE("paciente");

    private final String dbValue;

    Role(String dbValue) {
        this.dbValue = dbValue;
    }

    @JsonValue
    public String getDbValue() {
        return dbValue;
    }

    @Override
    public String toString() {
        return dbValue;
    }

    public static Role fromDbValue(String dbValue) {
        for (Role role : Role.values()) {
            if (role.dbValue.equalsIgnoreCase(dbValue)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Rol desconocido: " + dbValue);
    }
}



