package com.clinica.dental_back_spring.controller;

import com.clinica.dental_back_spring.dto.CreateProfessionalRequest;
import com.clinica.dental_back_spring.dto.ProfessionalDTO;
import com.clinica.dental_back_spring.dto.UpdateProfessionalRequest;
import com.clinica.dental_back_spring.service.ProfessionalService;
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
@RequestMapping("/professionals")
@Tag(name = "Profesionales", description = "Gestión de profesionales de la clínica")
public class ProfessionalController {

    private final ProfessionalService professionalService;

    public ProfessionalController(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    // ==========================================================
    // 🔹 GET /professionals?query=
    // ==========================================================
    @Operation(summary = "Listar profesionales", description = "Devuelve todos los profesionales o filtra por nombre o especialidad/licencia.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<ProfessionalDTO>> list(@RequestParam(required = false) String query) {
        List<ProfessionalDTO> list = professionalService.findAll(query);
        return ResponseEntity.ok(list);
    }

    // ==========================================================
    // 🔹 GET /professionals/:id
    // ==========================================================
    @Operation(summary = "Obtener profesional por ID")
    @ApiResponse(responseCode = "200", description = "Profesional encontrado")
    @ApiResponse(responseCode = "404", description = "Profesional no encontrado", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            ProfessionalDTO dto = professionalService.findById(id);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    // ==========================================================
    // 🔹 POST /professionals
    // ==========================================================
    @Operation(summary = "Crear profesional")
    @ApiResponse(responseCode = "201", description = "Profesional creado correctamente")
    @ApiResponse(responseCode = "400", description = "Error en los datos", content = @Content)
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateProfessionalRequest req) {
        try {
            ProfessionalDTO created = professionalService.create(req);
            return ResponseEntity.status(201).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // ==========================================================
    // 🔹 PUT /professionals/:id
    // ==========================================================
    @Operation(summary = "Actualizar profesional")
    @ApiResponse(responseCode = "200", description = "Profesional actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Profesional no encontrado", content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProfessionalRequest req
    ) {
        try {
            ProfessionalDTO updated = professionalService.update(id, req);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    // ==========================================================
    // 🔹 DELETE /professionals/:id (soft delete)
    // ==========================================================
    @Operation(summary = "Eliminar profesional", description = "Marca un profesional como inactivo (soft delete).")
    @ApiResponse(responseCode = "204", description = "Profesional desactivado correctamente")
    @ApiResponse(responseCode = "404", description = "Profesional no encontrado", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            professionalService.softDelete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }
}
