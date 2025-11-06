package com.clinica.dental_back_spring.controller;

import com.clinica.dental_back_spring.dto.CreatePatientRequest;
import com.clinica.dental_back_spring.dto.PatientDTO;
import com.clinica.dental_back_spring.dto.UpdatePatientRequest;
import com.clinica.dental_back_spring.service.PatientService;
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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/patients")
@Tag(name = "Patients", description = "Gestión de pacientes: CRUD y búsquedas")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Listar pacientes (opcionalmente filtrando por query)
     */
    @Operation(summary = "Listar pacientes", description = "Devuelve la lista de pacientes. Se puede filtrar por texto con el parámetro 'query' (nombre, apellidos, nif o email).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pacientes",
                    content = @Content(schema = @Schema(implementation = PatientDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<PatientDTO>> list(
            @Parameter(description = "Texto de búsqueda (opcional)") @RequestParam(required = false) String query) {
        List<PatientDTO> list = patientService.findAll(query);
        return ResponseEntity.ok(list);
    }

    /**
     * Obtener paciente por id
     */
    @Operation(summary = "Obtener paciente por id", description = "Devuelve los datos del paciente indicado por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente encontrado",
                    content = @Content(schema = @Schema(implementation = PatientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Patient not found\"}")))
    })

    @GetMapping("/{id}")
    public ResponseEntity<?> get(
            @Parameter(description = "ID del paciente", example = "1") @PathVariable Long id) {
        try {
            PatientDTO dto = patientService.findById(id);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        }
    }

    /**
     * Crear paciente
     */
    @Operation(summary = "Crear paciente", description = "Crea un nuevo paciente con los datos proporcionados.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Paciente creado",
                    content = @Content(schema = @Schema(implementation = PatientDTO.class),
                            examples = @ExampleObject(value = "{\"id\":1,\"nif\":\"12345678A\",\"firstName\":\"Juan\",\"lastName\":\"Pérez\",\"email\":\"juan@example.com\",\"phone\":\"600123456\",\"active\":true}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la petición",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Validation error\"}")))
    })
    @PostMapping
    public ResponseEntity<?> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos para crear un paciente",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreatePatientRequest.class),
                            examples = @ExampleObject(value = "{\"nif\":\"12345678A\",\"firstName\":\"Juan\",\"lastName\":\"Pérez\",\"email\":\"juan@example.com\",\"phone\":\"600123456\"}"))
            )
            @Valid @RequestBody CreatePatientRequest req) {
        PatientDTO created = patientService.create(req);
        return ResponseEntity.status(201).body(created);
    }


    /**
     * Actualizar paciente
     */
    @Operation(summary = "Actualizar paciente", description = "Actualiza los campos del paciente indicado (actualización parcial).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente actualizado",
                    content = @Content(schema = @Schema(implementation = PatientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Patient not found\"}")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Parameter(description = "ID del paciente", example = "1") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Campos para actualizar el paciente",
                    content = @Content(schema = @Schema(implementation = UpdatePatientRequest.class),
                            examples = @ExampleObject(value = "{\"firstName\":\"Nuevo\",\"phone\":\"600000000\"}"))
            )
            @Valid @RequestBody UpdatePatientRequest req) {
        try {
            PatientDTO updated = patientService.update(id, req);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(Map.of("message", ex.getMessage()));
        }
    }

    /**
     * Borrado lógico de paciente (soft delete)
     */
    @Operation(summary = "Eliminar paciente (soft)", description = "Marca al paciente como inactivo (soft delete).")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Paciente marcado como inactivo"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Patient not found\"}")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID del paciente", example = "1") @PathVariable Long id) {
        try {
            patientService.softDelete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(Map.of("message", ex.getMessage()));
        }
    }
}

