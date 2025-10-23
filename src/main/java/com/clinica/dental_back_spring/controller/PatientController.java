package com.clinica.dental_back_spring.controller;

import com.clinica.dental_back_spring.dto.CreatePatientRequest;
import com.clinica.dental_back_spring.dto.PatientDTO;
import com.clinica.dental_back_spring.dto.UpdatePatientRequest;
import com.clinica.dental_back_spring.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // GET /api/v1/patients?query=
    @GetMapping
    public ResponseEntity<List<PatientDTO>> list(@RequestParam(required = false) String query) {
        List<PatientDTO> list = patientService.findAll(query);
        return ResponseEntity.ok(list);
    }

    // GET /api/v1/patients/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            PatientDTO dto = patientService.findById(id);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        }
    }

    // POST /api/v1/patients
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreatePatientRequest req) {
        PatientDTO created = patientService.create(req);
        return ResponseEntity.status(201).body(created);
    }

    // PUT /api/v1/patients/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UpdatePatientRequest req) {
        try {
            PatientDTO updated = patientService.update(id, req);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        }
    }

    // DELETE (soft) /api/v1/patients/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            patientService.softDelete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        }
    }
}

