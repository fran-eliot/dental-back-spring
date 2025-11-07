package com.clinica.dental_back_spring.controller;

import com.clinica.dental_back_spring.dto.CreateTreatmentRequest;
import com.clinica.dental_back_spring.dto.TreatmentDTO;
import com.clinica.dental_back_spring.dto.UpdateTreatmentRequest;
import com.clinica.dental_back_spring.service.TreatmentService;
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

@RestController
@RequestMapping("/api/v1/treatments")
@Tag(name = "Treatments", description = "Gestión de tratamientos: listado, creación, edición y visibilidad")
public class TreatmentController {

    private final TreatmentService treatmentService;

    public TreatmentController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    /**
     * Listar tratamientos (opcional filtro texto y visibleOnly)
     */
    @Operation(summary = "Listar tratamientos",
            description = "Devuelve la lista de tratamientos. Se puede filtrar por texto (nombre o tipo) y por visibilidad (visibleOnly=true).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de tratamientos",
                    content = @Content(schema = @Schema(implementation = TreatmentDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<TreatmentDTO>> list(
            @Parameter(description = "Texto de búsqueda (opcional)") @RequestParam(required = false) String query,
            @Parameter(description = "Mostrar solo tratamientos visibles (opcional)") @RequestParam(required = false) Boolean visibleOnly) {
        List<TreatmentDTO> list = treatmentService.findAll(query, visibleOnly);
        return ResponseEntity.ok(list);
    }

    /**
     * Obtener tratamiento por id
     */
    @Operation(summary = "Obtener tratamiento por id", description = "Devuelve los datos del tratamiento indicado por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tratamiento encontrado",
                    content = @Content(schema = @Schema(implementation = TreatmentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tratamiento no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Treatment not found\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@Parameter(description = "ID del tratamiento", example = "1") @PathVariable Long id) {
        try {
            TreatmentDTO dto = treatmentService.findById(id);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        }
    }

    /**
     * Crear tratamiento
     */
    @Operation(summary = "Crear tratamiento", description = "Crea un nuevo tratamiento con los datos proporcionados.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tratamiento creado",
                    content = @Content(schema = @Schema(implementation = TreatmentDTO.class),
                            examples = @ExampleObject(value = "{\"id\":1,\"name\":\"Limpieza\",\"type\":\"Preventivo\",\"duration\":30,\"price\":30.0,\"visible\":true}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la petición",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Validation error\"}")))
    })
    @PostMapping
    public ResponseEntity<?> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos para crear el tratamiento",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateTreatmentRequest.class),
                            examples = @ExampleObject(value = "{\"name\":\"Limpieza\",\"type\":\"Preventivo\",\"duration\":30,\"price\":30.0,\"visible\":true}"))
            )
            @Valid @RequestBody CreateTreatmentRequest req){
        TreatmentDTO created = treatmentService.create(req);
        return ResponseEntity.status(201).body(created);
    }

    /**
     * Actualizar tratamiento
     */
    @Operation(summary = "Actualizar tratamiento", description = "Actualiza los campos del tratamiento indicado (actualización parcial).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tratamiento actualizado",
                    content = @Content(schema = @Schema(implementation = TreatmentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tratamiento no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Treatment not found\"}")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?>  update(
            @Parameter(description = "ID del tratamiento", example = "1") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Campos para actualizar el tratamiento",
                    content = @Content(schema = @Schema(implementation = UpdateTreatmentRequest.class),
                            examples = @ExampleObject(value = "{\"name\":\"Limpieza profunda\",\"price\":45.0}"))
            )
            @Valid @RequestBody UpdateTreatmentRequest req) {
        try {
            TreatmentDTO updated = treatmentService.update(id, req);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        }
    }

    /**
     * Borrado lógico de tratamiento (soft delete -> visible=false)
     */
    @Operation(summary = "Eliminar tratamiento (soft)", description = "Marca el tratamiento como no visible (soft delete).")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tratamiento marcado como no visible"),
            @ApiResponse(responseCode = "404", description = "Tratamiento no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Treatment not found\"}")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID del tratamiento", example = "1") @PathVariable Long id)  {
        try {
            treatmentService.softDelete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        }
    }
}

