package com.clinica.dental_back_spring.controller;

import com.clinica.dental_back_spring.dto.CreateTreatmentRequest;
import com.clinica.dental_back_spring.dto.TreatmentDTO;
import com.clinica.dental_back_spring.dto.UpdateTreatmentRequest;
import com.clinica.dental_back_spring.service.TreatmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/treatments")
public class TreatmentController {

    private final TreatmentService treatmentService;

    public TreatmentController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    // GET /api/v1/treatments?query=&visibleOnly=true
    @GetMapping
    public ResponseEntity<List<TreatmentDTO>> list(@RequestParam(required = false) String query,
                                                   @RequestParam(required = false) Boolean visibleOnly) {
        List<TreatmentDTO> list = treatmentService.findAll(query, visibleOnly);
        return ResponseEntity.ok(list);
    }

    // GET /api/v1/treatments/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            TreatmentDTO dto = treatmentService.findById(id);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        }
    }

    // POST /api/v1/treatments
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateTreatmentRequest req) {
        TreatmentDTO created = treatmentService.create(req);
        return ResponseEntity.status(201).body(created);
    }

    // PUT /api/v1/treatments/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UpdateTreatmentRequest req) {
        try {
            TreatmentDTO updated = treatmentService.update(id, req);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        }
    }

    // DELETE (soft) /api/v1/treatments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            treatmentService.softDelete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        }
    }
}

