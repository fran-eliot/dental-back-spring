package com.clinica.dental_back_spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "professionals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Professional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_professionals")
    private Long id;

    @Column(name = "nif_professionals")
    private String nif;

    @Column(name ="license_number_professionals")
    private String license;

    @Column(name = "name_professionals")
    private String name;

    @Column(name= "last_name_professionals")
    private String lastName;

    @Column(name = "phone_professionals")
    private String phone;

    @Column(name = "email_professionals",unique=true)
    private String email;

    @Column(name ="assigned_room_professionals")
    private String room;

    @Column(name = "is_active_professionals")
    private boolean active = true;

    // Relación opcional con User (1-1)
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id_users" )
    private User user;

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Availability> availabilities;

    @OneToMany(mappedBy = "professional",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments;


}
