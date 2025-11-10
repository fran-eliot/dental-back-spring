package com.clinica.dental_back_spring.entity;

import com.clinica.dental_back_spring.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_users")
    private Long id;

    @Column(name = "username_users",unique=true)
    private String email;

    @Column(name = "password_users")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol_users")
    private Role role;

    @Column(name ="is_active_users")
    private boolean active = true;


}

