package com.clinica.dental_back_spring.controller;

import com.clinica.dental_back_spring.dto.CreateAppointmentRequest;
import com.clinica.dental_back_spring.entity.Appointment;
import com.clinica.dental_back_spring.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
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
@RequestMapping("/api/v1/appointments")
@Tag(name = "Appointments", description = "Gestión de citas médicas (reservas, estados, cancelaciones, etc.)")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // ==========================================================
    // 🩺 Crear una nueva cita
    // ==========================================================
    @Operation(
            summary = "Crear una cita",
            description = "Crea una nueva cita asignando un paciente, profesional, tratamiento y slot disponibles. "
                    + "Si el slot ya está ocupado, devuelve un error 409 (Conflict)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cita creada correctamente",
                    content = @Content(schema = @Schema(implementation = Appointment.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
                    content = @Content(schema = @Schema(example = "{\"message\": \"Patient not found\"}"))),
            @ApiResponse(responseCode = "409", description = "El slot ya está reservado",
                    content = @Content(schema = @Schema(example = "{\"message\": \"Slot already booked\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody
                                        @Parameter(description = "Datos para la creación de la cita") CreateAppointmentRequest req) {
        try {
            Appointment created = appointmentService.createAppointment(req);
            return ResponseEntity.ok(created);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(Map.of("message", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "internal error"));
        }
    }

    // ==========================================================
    // 👤 Listar citas por paciente
    // ==========================================================
    @Operation(
            summary = "Obtener las citas de un paciente",
            description = "Devuelve todas las citas asociadas a un paciente dado su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Citas encontradas",
                    content = @Content(schema = @Schema(implementation = Appointment.class))),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    @GetMapping("/by-patient/{patientId}")
    public ResponseEntity<List<Appointment>> byPatient(
            @Parameter(description = "ID del paciente", example = "1") @PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId));
    }

    // ==========================================================
    // 👨‍⚕️ Listar citas por profesional
    // ==========================================================
    @Operation(
            summary = "Obtener las citas de un profesional",
            description = "Devuelve todas las citas asignadas a un profesional específico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Citas encontradas",
                    content = @Content(schema = @Schema(implementation = Appointment.class))),
            @ApiResponse(responseCode = "404", description = "Profesional no encontrado")
    })
    @GetMapping("/by-professional/{professionalId}")
    public ResponseEntity<List<Appointment>> byProfessional(
            @Parameter(description = "ID del profesional", example = "3") @PathVariable Long professionalId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByProfessional(professionalId));
    }

    // ==========================================================
    // 🔁 Actualizar estado de cita
    // ==========================================================
    @Operation(
            summary = "Actualizar el estado de una cita",
            description = "Permite cambiar el estado de una cita (Pendiente, Confirmada, Realizada, Cancelada, etc.)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = Appointment.class))),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<Appointment> updateStatus(
            @Parameter(description = "ID de la cita", example = "5") @PathVariable Long appointmentId,
            @Parameter(description = "Nuevo estado", example = "CONFIRMADA") @RequestParam String status) {
        return ResponseEntity.ok(
                appointmentService.updateStatus(appointmentId,
                        com.clinica.dental_back_spring.enums.AppointmentStatus.valueOf(status.toUpperCase()))
        );
    }

    // ==========================================================
    // ❌ Cancelar cita
    // ==========================================================
    @Operation(
            summary = "Cancelar una cita",
            description = "Marca una cita como cancelada y permite indicar el motivo de la cancelación."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cita cancelada correctamente",
                    content = @Content(schema = @Schema(implementation = Appointment.class))),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @PutMapping("/{appointmentId}/cancel")
    public ResponseEntity<Appointment> cancel(
            @Parameter(description = "ID de la cita", example = "10") @PathVariable Long appointmentId,
            @Parameter(description = "Motivo de la cancelación") @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(appointmentId, reason));
    }
}

