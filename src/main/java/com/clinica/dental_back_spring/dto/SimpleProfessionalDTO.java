package com.clinica.dental_back_spring.dto;

import com.clinica.dental_back_spring.entity.Professional;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Información simplificada del profesional asignado")
public class SimpleProfessionalDTO {

    @Schema(description = "Identificador del profesional", example = "2")
    private Long id;

    @Schema(description = "Nombre del profesional", example = "Laura")
    private String name;

    @Schema(description = "Apellido del profesional", example = "Gómez")
    private String lastName;

    public static SimpleProfessionalDTO from(Professional p) {
        if (p == null) return null;
        return SimpleProfessionalDTO.builder()
                .id(p.getId())
                .name(p.getName())
                .lastName(p.getLastName())
                .build();
    }
}
