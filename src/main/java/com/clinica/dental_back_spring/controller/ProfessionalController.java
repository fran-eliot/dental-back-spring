package com.clinica.dental_back_spring.controller;

import com.clinica.dental_back_spring.dto.CreateProfessionalRequest;
import com.clinica.dental_back_spring.dto.ProfessionalDTO;
import com.clinica.dental_back_spring.dto.UpdateProfessionalRequest;
import com.clinica.dental_back_spring.service.ProfessionalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/professionals")
public class ProfessionalController {

    private final ProfessionalService professionalService;

    public ProfessionalController(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    // GET /api/v1/professionals?query=
    @GetMapping
    public ResponseEntity<List<ProfessionalDTO>> list(@RequestParam(required = false) String query) {
        List<ProfessionalDTO> list = professionalService.findAll(query);
        return ResponseEntity.ok(list);
    }

    // GET /api/v1/professionals/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            ProfessionalDTO dto = professionalService.findById(id);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        }
    }

    // POST /api/v1/professionals
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateProfessionalRequest req) {
        ProfessionalDTO created = professionalService.create(req);
        return ResponseEntity.status(201).body(created);
    }

    // PUT /api/v1/professionals/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UpdateProfessionalRequest req) {
        try {
            ProfessionalDTO updated = professionalService.update(id, req);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        }
    }

    // DELETE (soft) /api/v1/professionals/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            professionalService.softDelete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        }
    }
}
