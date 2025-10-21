package com.clinica.dental_back_spring.entity;

import com.clinica.dental_back_spring.enums.Period;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "slots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_slots")
    private Long id;

    @Column(name = "start_time_slots")
    private LocalTime startTime;

    @Column(name = "end_time_slots")
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name= "period")
    private Period period;

    @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Availability> availabilities;


}

