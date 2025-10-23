package com.clinica.dental_back_spring.entity;

import com.clinica.dental_back_spring.enums.StatusAvailability;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(name = "professional_availabilities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_professional_availabilities")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id")
    private Professional professional;

    @Column(name = "date_availability")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_availability")
    private StatusAvailability status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "id_slots")
    private Slot slot;

}

