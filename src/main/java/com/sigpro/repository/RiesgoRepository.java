package com.sigpro.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.Riesgo;

public interface RiesgoRepository extends MongoRepository<Riesgo, String> {

	List<Riesgo> findByProyectoId(String proyectoId);

	long countByProyectoIdAndEstadoNot(String proyectoId, String estado);
}
