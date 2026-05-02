package com.sigpro.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.DisenoUi;

public interface DisenoUiRepository extends MongoRepository<DisenoUi, String> {

	List<DisenoUi> findByProyectoId(String proyectoId);
}
