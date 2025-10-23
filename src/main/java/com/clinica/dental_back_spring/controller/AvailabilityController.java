package com.clinica.dental_back_spring.controller;

import com.clinica.dental_back_spring.dto.AvailabilityDTO;
import com.clinica.dental_back_spring.dto.CreateAvailabilityRequest;
import com.clinica.dental_back_spring.dto.UpdateAvailabilityRequest;
import com.clinica.dental_back_spring.service.AvailabilityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/availabilities")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    // GET /api/v1/availabilities?professionalId=&date=yyyy-MM-dd
    @GetMapping
    public ResponseEntity<List<AvailabilityDTO>> list(@RequestParam Long professionalId,
                                                      @RequestParam(required = false) String date) {
        LocalDate d = date != null ? LocalDate.parse(date) : null;
        if (d != null) {
            return ResponseEntity.ok(availabilityService.findByProfessionalAndDate(professionalId, d));
        } else {
            return ResponseEntity.ok(availabilityService.findAll());
        }
    }

    // GET /api/v1/availabilities/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            AvailabilityDTO dto = availabilityService.findById(id);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        }
    }

    // POST /api/v1/availabilities
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateAvailabilityRequest req) {
        try {
            AvailabilityDTO created = availabilityService.create(req);
            return ResponseEntity.status(201).body(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", ex.getMessage()));
        }
    }

    // PUT /api/v1/availabilities/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UpdateAvailabilityRequest req) {
        try {
            AvailabilityDTO updated = availabilityService.update(id, req);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        }
    }

    // DELETE (soft) /api/v1/availabilities/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            availabilityService.softDelete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        }
    }
}

