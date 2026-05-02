package com.sigpro.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.ComponenteUi;

public interface ComponenteUiRepository extends MongoRepository<ComponenteUi, String> {

	List<ComponenteUi> findByDesignSystemId(String designSystemId);

	List<ComponenteUi> findByProyectoId(String proyectoId);
}
