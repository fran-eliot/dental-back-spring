package com.clinica.dental_back_spring.entity;

import com.clinica.dental_back_spring.enums.AppointmentStatus;
import com.clinica.dental_back_spring.enums.CreatedBy;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_appointments")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id")
    private Slot slot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id")
    private Professional professional;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id")
    private Treatment treatment;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_appointments")
    private AppointmentStatus status;

    @Column(name = "cancellation_reason_appointments", columnDefinition = "TEXT")
    private String cancellationReason;

    @Column(name = "date_appointments")
    private LocalDateTime date;

    @Column(name = "duration_minutes_appointments")
    private Integer duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "created_by_appointments")
    private CreatedBy createdBy;
}

