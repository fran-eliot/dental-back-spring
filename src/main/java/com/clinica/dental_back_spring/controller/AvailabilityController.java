package com.clinica.dental_back_spring.controller;

import com.clinica.dental_back_spring.dto.AvailabilityDTO;
import com.clinica.dental_back_spring.dto.CreateAvailabilityRequest;
import com.clinica.dental_back_spring.dto.UpdateAvailabilityRequest;
import com.clinica.dental_back_spring.service.AvailabilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/availabilities")
@Tag(name = "Disponibilidades", description = "Gestión de disponibilidades de los profesionales")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    // ==========================================================
    // 🔹 GET /availabilities?professionalId=&date=
    // ==========================================================
    @Operation(summary = "Listar disponibilidades", description = "Devuelve todas las disponibilidades o filtra por profesional y fecha.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<AvailabilityDTO>> list(
            @RequestParam(required = false) Long professionalId,
            @RequestParam(required = false) String date
    ) {
        if (professionalId != null && date != null) {
            LocalDate parsedDate = LocalDate.parse(date);
            return ResponseEntity.ok(availabilityService.findByProfessionalAndDate(professionalId, parsedDate));
        }
        return ResponseEntity.ok(availabilityService.findAll());
    }

    // ==========================================================
    // 🔹 GET /availabilities/:id
    // ==========================================================
    @Operation(summary = "Obtener disponibilidad por ID")
    @ApiResponse(responseCode = "200", description = "Disponibilidad encontrada")
    @ApiResponse(responseCode = "404", description = "No encontrada", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            AvailabilityDTO dto = availabilityService.findById(id);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    // ==========================================================
    // 🔹 POST /availabilities
    // ==========================================================
    @Operation(summary = "Crear disponibilidad", description = "Crea una nueva disponibilidad para un profesional.")
    @ApiResponse(responseCode = "201", description = "Disponibilidad creada correctamente")
    @ApiResponse(responseCode = "400", description = "Error en los datos o conflicto", content = @Content)
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateAvailabilityRequest req) {
        try {
            AvailabilityDTO created = availabilityService.create(req);
            return ResponseEntity.status(201).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // ==========================================================
    // 🔹 PUT /availabilities/:id
    // ==========================================================
    @Operation(summary = "Actualizar disponibilidad", description = "Actualiza el estado o la información de una disponibilidad.")
    @ApiResponse(responseCode = "200", description = "Disponibilidad actualizada correctamente")
    @ApiResponse(responseCode = "404", description = "No encontrada", content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAvailabilityRequest req
    ) {
        try {
            AvailabilityDTO updated = availabilityService.update(id, req);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    // ==========================================================
    // 🔹 DELETE /availabilities/:id
    // ==========================================================
    @Operation(summary = "Eliminar disponibilidad", description = "Marca una disponibilidad como no disponible (soft delete).")
    @ApiResponse(responseCode = "204", description = "Eliminada correctamente")
    @ApiResponse(responseCode = "404", description = "No encontrada", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            availabilityService.softDelete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }
}

