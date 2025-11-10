package com.clinica.dental_back_spring.controller;

import com.clinica.dental_back_spring.dto.CreatePatientRequest;
import com.clinica.dental_back_spring.dto.PatientDTO;
import com.clinica.dental_back_spring.dto.UpdatePatientRequest;
import com.clinica.dental_back_spring.service.PatientService;
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
@RequestMapping("/patients")
@Tag(name = "Pacientes", description = "Gestión de pacientes de la clínica")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // ==========================================================
    // 🔹 GET /patients?query=
    // ==========================================================
    @Operation(summary = "Listar pacientes", description = "Devuelve todos los pacientes o filtra por nombre o NIF.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<PatientDTO>> list(@RequestParam(required = false) String query) {
        List<PatientDTO> patients = patientService.findAll(query);
        return ResponseEntity.ok(patients);
    }

    // ==========================================================
    // 🔹 GET /patients/:id
    // ==========================================================
    @Operation(summary = "Obtener paciente por ID")
    @ApiResponse(responseCode = "200", description = "Paciente encontrado")
    @ApiResponse(responseCode = "404", description = "Paciente no encontrado", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            PatientDTO dto = patientService.findById(id);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    // ==========================================================
    // 🔹 POST /patients
    // ==========================================================
    @Operation(summary = "Crear nuevo paciente")
    @ApiResponse(responseCode = "201", description = "Paciente creado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreatePatientRequest req) {
        try {
            PatientDTO created = patientService.create(req);
            return ResponseEntity.status(201).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // ==========================================================
    // 🔹 PUT /patients/:id
    // ==========================================================
    @Operation(summary = "Actualizar paciente")
    @ApiResponse(responseCode = "200", description = "Paciente actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Paciente no encontrado", content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UpdatePatientRequest req) {
        try {
            PatientDTO updated = patientService.update(id, req);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    // ==========================================================
    // 🔹 DELETE /patients/:id (soft delete)
    // ==========================================================
    @Operation(summary = "Eliminar paciente (soft delete)", description = "Marca al paciente como inactivo en lugar de eliminarlo físicamente.")
    @ApiResponse(responseCode = "204", description = "Paciente desactivado correctamente")
    @ApiResponse(responseCode = "404", description = "Paciente no encontrado", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            patientService.softDelete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }
}


