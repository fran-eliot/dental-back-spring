package com.clinica.dental_back_spring.controller;

import com.clinica.dental_back_spring.dto.CreateTreatmentRequest;
import com.clinica.dental_back_spring.dto.TreatmentDTO;
import com.clinica.dental_back_spring.dto.UpdateTreatmentRequest;
import com.clinica.dental_back_spring.service.TreatmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/treatments")
@Tag(name = "Tratamientos", description = "Gestión de tratamientos de la clínica")
public class TreatmentController {

    private final TreatmentService treatmentService;

    public TreatmentController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    // ==========================================================
    // 🔹 GET /treatments?query=&visibleOnly=true
    // ==========================================================
    @Operation(summary = "Listar tratamientos", description = "Devuelve todos los tratamientos o filtra por nombre/tipo y visibilidad.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<TreatmentDTO>> list(
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "false") boolean visibleOnly
    ) {
        List<TreatmentDTO> treatments = treatmentService.findAll(query, visibleOnly);
        return ResponseEntity.ok(treatments);
    }

    // ==========================================================
    // 🔹 GET /treatments/:id
    // ==========================================================
    @Operation(summary = "Obtener tratamiento por ID")
    @ApiResponse(responseCode = "200", description = "Tratamiento encontrado")
    @ApiResponse(responseCode = "404", description = "Tratamiento no encontrado", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            TreatmentDTO dto = treatmentService.findById(id);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    // ==========================================================
    // 🔹 POST /treatments
    // ==========================================================
    @Operation(summary = "Crear tratamiento")
    @ApiResponse(responseCode = "201", description = "Tratamiento creado correctamente")
    @ApiResponse(responseCode = "400", description = "Error en los datos", content = @Content)
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateTreatmentRequest req) {
        try {
            TreatmentDTO created = treatmentService.create(req);
            return ResponseEntity.status(201).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // ==========================================================
    // 🔹 PUT /treatments/:id
    // ==========================================================
    @Operation(summary = "Actualizar tratamiento")
    @ApiResponse(responseCode = "200", description = "Tratamiento actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Tratamiento no encontrado", content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTreatmentRequest req
    ) {
        try {
            TreatmentDTO updated = treatmentService.update(id, req);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    // ==========================================================
    // 🔹 DELETE /treatments/:id (soft delete)
    // ==========================================================
    @Operation(summary = "Eliminar tratamiento", description = "Marca un tratamiento como no visible (soft delete).")
    @ApiResponse(responseCode = "204", description = "Tratamiento marcado como no visible correctamente")
    @ApiResponse(responseCode = "404", description = "Tratamiento no encontrado", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            treatmentService.softDelete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }
}
