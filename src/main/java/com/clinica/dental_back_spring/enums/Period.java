package com.clinica.dental_back_spring.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Period {
    MANANA("mañana"),
    TARDE("tarde");

    private final String value;

    Period(String value){
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
