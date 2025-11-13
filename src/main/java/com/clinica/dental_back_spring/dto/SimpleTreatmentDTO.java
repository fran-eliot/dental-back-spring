package com.clinica.dental_back_spring.dto;

import com.clinica.dental_back_spring.entity.Treatment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Información simplificada del tratamiento realizado")
public class SimpleTreatmentDTO {
    @Schema(description = "Identificador del tratamiento", example = "1")
    private Long id;

    @Schema(description = "Nombre del tratamiento", example = "Limpieza dental")
    private String name;

    public static SimpleTreatmentDTO from(Treatment t) {
        if (t == null) return null;
        return SimpleTreatmentDTO.builder()
                .id(t.getId())
                .name(t.getName())
                .build();
    }
}