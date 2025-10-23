package com.clinica.dental_back_spring.controller;

import com.clinica.dental_back_spring.dto.CreateAppointmentRequest;
import com.clinica.dental_back_spring.entity.Appointment;
import com.clinica.dental_back_spring.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateAppointmentRequest req) {
        try {
            Appointment created = appointmentService.createAppointment(req);
            return ResponseEntity.ok(created);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body( // 409 Conflict
                    java.util.Map.of("message", e.getMessage())
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Map.of("message", "internal error"));
        }
    }

    // Ejemplo: listar por patient
    @GetMapping("/by-patient/{patientId}")
    public ResponseEntity<?> byPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId));
    }
}
}
