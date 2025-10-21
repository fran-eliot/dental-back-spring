package com.clinica.dental_back_spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "treatments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_treatments")
    private Long id;

    @Column(name = "name_treatments")
    private String name;

    @Column(name = "type_treatments")
    private String type;

    @Column(name = "duration_minutes_treatments")
    private Integer duration;

    @Column(name ="price_treatments")
    private double price;

    @Column(name = "visible_to_patients_treatments")
    private boolean visible = true;

    @OneToMany(mappedBy = "treatment", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Appointment> appointments;
}

