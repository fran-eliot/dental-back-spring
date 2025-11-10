package com.clinica.dental_back_spring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Información simplificada del paciente asociado")
public class SimplePatientDTO {
    @Schema(description = "Identificador del paciente", example = "5")
    private Long id;

    @Schema(description = "Nombre del paciente", example = "Lucía")
    private String firstName;

    @Schema(description = "Apellido del paciente", example = "Santos")
    private String lastName;

}
