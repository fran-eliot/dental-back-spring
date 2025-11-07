package com.clinica.dental_back_spring.controller;

import com.clinica.dental_back_spring.dto.AvailabilityDTO;
import com.clinica.dental_back_spring.dto.CreateAvailabilityRequest;
import com.clinica.dental_back_spring.dto.UpdateAvailabilityRequest;
import com.clinica.dental_back_spring.service.AvailabilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/availabilities")
@Tag(name = "Availabilities", description = "Gestión de disponibilidades de los profesionales")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    /**
     * Listar disponibilidades (por profesional y fecha opcional)
     */
    @Operation(
            summary = "Listar disponibilidades",
            description = "Devuelve la lista de disponibilidades. Se puede filtrar por ID de profesional y fecha (yyyy-MM-dd)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de disponibilidades obtenida correctamente",
                    content = @Content(schema = @Schema(implementation = AvailabilityDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<AvailabilityDTO>> list(
            @Parameter(description = "ID del profesional", example = "1") @RequestParam Long professionalId,
            @Parameter(description = "Fecha (formato yyyy-MM-dd)", example = "2025-11-07") @RequestParam(required = false) String date) {
        LocalDate d = date != null ? LocalDate.parse(date) : null;
        if (d != null) {
            return ResponseEntity.ok(availabilityService.findByProfessionalAndDate(professionalId, d));
        } else {
            return ResponseEntity.ok(availabilityService.findAll());
        }
    }

    /**
     * Obtener disponibilidad por id
     */
    @Operation(
            summary = "Obtener disponibilidad por ID",
            description = "Devuelve la información detallada de una disponibilidad específica."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Disponibilidad encontrada",
                    content = @Content(schema = @Schema(implementation = AvailabilityDTO.class))),
            @ApiResponse(responseCode = "404", description = "Disponibilidad no encontrada",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Availability not found\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> get(
            @Parameter(description = "ID de la disponibilidad", example = "5") @PathVariable Long id) {
        try {
            AvailabilityDTO dto = availabilityService.findById(id);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        }
    }

    /**
     * Crear nueva disponibilidad
     */
    @Operation(
            summary = "Crear disponibilidad",
            description = "Crea una nueva disponibilidad para un profesional en una fecha y horario concretos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Disponibilidad creada correctamente",
                    content = @Content(schema = @Schema(implementation = AvailabilityDTO.class),
                            examples = @ExampleObject(value = "{\"id\":1,\"professionalId\":2,\"slotId\":3,\"date\":\"2025-11-10\",\"status\":\"LIBRE\"}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o conflicto de disponibilidad",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Slot already booked\"}")))
    })
    @PostMapping
    public ResponseEntity<?> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos para crear la disponibilidad",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateAvailabilityRequest.class),
                            examples = @ExampleObject(value = "{\"professionalId\":2,\"slotId\":3,\"date\":\"2025-11-10\",\"status\":\"LIBRE\"}"))
            )
            @Valid @RequestBody CreateAvailabilityRequest req) {
        try {
            AvailabilityDTO created = availabilityService.create(req);
            return ResponseEntity.status(201).body(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", ex.getMessage()));
        }
    }

    /**
     * Actualizar disponibilidad
     */
    @Operation(
            summary = "Actualizar disponibilidad",
            description = "Actualiza los datos o el estado de una disponibilidad concreta."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Disponibilidad actualizada correctamente",
                    content = @Content(schema = @Schema(implementation = AvailabilityDTO.class))),
            @ApiResponse(responseCode = "404", description = "Disponibilidad no encontrada",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Availability not found\"}")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Parameter(description = "ID de la disponibilidad", example = "1") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos para actualizar la disponibilidad",
                    content = @Content(schema = @Schema(implementation = UpdateAvailabilityRequest.class),
                            examples = @ExampleObject(value = "{\"status\":\"NO_DISPONIBLE\"}"))
            )
            @Valid @RequestBody UpdateAvailabilityRequest req) {
        try {
            AvailabilityDTO updated = availabilityService.update(id, req);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        }
    }

    /**
     * Borrado lógico de disponibilidad
     */
    @Operation(
            summary = "Eliminar disponibilidad (soft delete)",
            description = "Marca la disponibilidad como no activa o elimina su registro lógico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Disponibilidad eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Disponibilidad no encontrada",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Availability not found\"}")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID de la disponibilidad", example = "1") @PathVariable Long id) {
        try {
            availabilityService.softDelete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        }
    }
}

