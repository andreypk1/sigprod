package com.sigpro.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.DesignSystem;

public interface DesignSystemRepository extends MongoRepository<DesignSystem, String> {

	Optional<DesignSystem> findFirstByProyectoId(String proyectoId);
}
