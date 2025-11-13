package com.clinica.dental_back_spring.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BirthDateRepository extends MongoRepository<BirthDateDocument, String> {

    Optional<BirthDateDocument> findByUserId(Long userId);

}

