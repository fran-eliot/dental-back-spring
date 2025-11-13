package com.clinica.dental_back_spring.controller;

import com.clinica.dental_back_spring.dto.AppointmentDTO;
import com.clinica.dental_back_spring.dto.CreateAppointmentRequest;
import com.clinica.dental_back_spring.dto.UpdateAppointmentRequest;
import com.clinica.dental_back_spring.enums.AppointmentStatus;
import com.clinica.dental_back_spring.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/appointments")
@Tag(name = "Citas", description = "Gestión de citas de pacientes y profesionales")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // ==========================================================
    // 🩺 POST /appointments → Crear cita
    // ==========================================================
    @Operation(summary = "Crear una cita")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateAppointmentRequest req) {
        try {
            AppointmentDTO created = appointmentService.createAppointment(req);
            return ResponseEntity.status(201).body(created);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(Map.of("message", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // ==========================================================
    // 👤 GET /appointments/by-patient/:id
    // ==========================================================
    @Operation(summary = "Listar citas por paciente")
    @GetMapping("/by-patient/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId));
    }

    // ==========================================================
    // 👨‍⚕️ GET /appointments/by-professional/:id
    // ==========================================================
    @Operation(summary = "Listar citas por profesional")
    @GetMapping("/by-professional/{professionalId}")
    public ResponseEntity<List<AppointmentDTO>> getByProfessional(@PathVariable Long professionalId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByProfessional(professionalId));
    }

    // ==========================================================
    // 🔁 PATCH /appointments/:id/status → Cambiar estado
    // ==========================================================
    @Operation(summary = "Actualizar estado de cita")
    @PatchMapping("/{appointmentId}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long appointmentId,
            @Valid @RequestBody UpdateAppointmentRequest req
    ) {
        try {
            AppointmentDTO updated = appointmentService.updateStatus(appointmentId, req.getStatus());
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // ==========================================================
    // ❌ DELETE /appointments/:id → Cancelar cita
    // ==========================================================
    @Operation(summary = "Cancelar cita")
    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<?> cancelAppointment(
            @PathVariable Long appointmentId,
            @RequestBody(required = false) Map<String, String> body
    ) {
        String reason = (body != null)
                ? body.getOrDefault("reason", "Sin motivo especificado")
                : "Sin motivo especificado";

        try {
            AppointmentDTO cancelled = appointmentService.cancelAppointment(appointmentId, reason);
            return ResponseEntity.ok(cancelled);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }
}
