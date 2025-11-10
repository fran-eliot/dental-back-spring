package com.clinica.dental_back_spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_patients")
    private Long id;

    @Column(name= "nif_patients")
    private String nif;

    @Column(name = "name_patients")
    private String firstName;

    @Column(name = "last_name_patients")
    private String lastName;

    @Column(name = "email_patients", unique=true)
    private String email;

    @Column(name = "phone_patients")
    private String phone;

    @Column(name ="is_active_patients")
    private boolean active = true;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Appointment> appointments;
}

