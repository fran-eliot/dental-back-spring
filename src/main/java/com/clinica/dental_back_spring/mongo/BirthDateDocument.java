package com.clinica.dental_back_spring.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "user_birthdates")
public class BirthDateDocument {

    @Id
    private String id;  // Mongo genera strings ObjectId

    private Long userId; // referencia opcional al usuario MySQL

    private LocalDate birthDate;

    private String note; // para rellenar si te piden más campos
}

