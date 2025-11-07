package com.clinica.dental_back_spring.controller;

import com.clinica.dental_back_spring.dto.CreateProfessionalRequest;
import com.clinica.dental_back_spring.dto.ProfessionalDTO;
import com.clinica.dental_back_spring.dto.UpdateProfessionalRequest;
import com.clinica.dental_back_spring.service.ProfessionalService;
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
@RequestMapping("/api/v1/professionals")
@Tag(name = "Professionals", description = "Gestión de profesionales: listado, creación, edición y borrado lógico")
public class ProfessionalController {

    private final ProfessionalService professionalService;

    public ProfessionalController(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    /**
     * Listar profesionales (con filtro opcional)
     */
    @Operation(
            summary = "Listar profesionales",
            description = "Devuelve la lista de profesionales registrados. Puede filtrarse por texto en nombre o especialidad."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de profesionales obtenida correctamente",
                    content = @Content(schema = @Schema(implementation = ProfessionalDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<ProfessionalDTO>> list(
            @Parameter(description = "Texto de búsqueda (opcional)") @RequestParam(required = false) String query) {
        List<ProfessionalDTO> list = professionalService.findAll(query);
        return ResponseEntity.ok(list);
    }

    /**
     * Obtener profesional por ID
     */
    @Operation(
            summary = "Obtener profesional por ID",
            description = "Devuelve los datos del profesional indicado por su identificador único."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profesional encontrado",
                    content = @Content(schema = @Schema(implementation = ProfessionalDTO.class))),
            @ApiResponse(responseCode = "404", description = "Profesional no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Professional not found\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> get(
            @Parameter(description = "ID del profesional", example = "1") @PathVariable Long id) {
        try {
            ProfessionalDTO dto = professionalService.findById(id);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        }
    }

    /**
     * Crear nuevo profesional
     */
    @Operation(
            summary = "Crear profesional",
            description = "Crea un nuevo profesional en la base de datos con los datos proporcionados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Profesional creado correctamente",
                    content = @Content(schema = @Schema(implementation = ProfessionalDTO.class),
                            examples = @ExampleObject(value = "{\"id\":1,\"firstName\":\"Laura\",\"lastName\":\"Gómez\",\"specialty\":\"Implantología\",\"active\":true}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la petición",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Validation error\"}")))
    })
    @PostMapping
    public ResponseEntity<?> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos para crear el profesional",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateProfessionalRequest.class),
                            examples = @ExampleObject(value = "{\"firstName\":\"Laura\",\"lastName\":\"Gómez\",\"specialty\":\"Implantología\"}"))
            )
            @Valid @RequestBody CreateProfessionalRequest req) {
        ProfessionalDTO created = professionalService.create(req);
        return ResponseEntity.status(201).body(created);
    }

    /**
     * Actualizar profesional
     */
    @Operation(
            summary = "Actualizar profesional",
            description = "Actualiza los datos del profesional especificado. Solo se modifican los campos enviados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profesional actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = ProfessionalDTO.class))),
            @ApiResponse(responseCode = "404", description = "Profesional no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Professional not found\"}")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Parameter(description = "ID del profesional", example = "1") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Campos para actualizar el profesional",
                    content = @Content(schema = @Schema(implementation = UpdateProfessionalRequest.class),
                            examples = @ExampleObject(value = "{\"specialty\":\"Ortodoncia\",\"active\":true}"))
            )
            @Valid @RequestBody UpdateProfessionalRequest req) {
        try {
            ProfessionalDTO updated = professionalService.update(id, req);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        }
    }

    /**
     * Borrado lógico (soft delete)
     */
    @Operation(
            summary = "Eliminar profesional (soft delete)",
            description = "Marca el profesional como inactivo en lugar de eliminarlo físicamente de la base de datos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Profesional marcado como inactivo"),
            @ApiResponse(responseCode = "404", description = "Profesional no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Professional not found\"}")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID del profesional", example = "1") @PathVariable Long id) {
        try {
            professionalService.softDelete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        }
    }
}
