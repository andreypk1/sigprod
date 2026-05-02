package com.sigpro.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.Incidente;

public interface IncidenteRepository extends MongoRepository<Incidente, String> {

	List<Incidente> findByProyectoId(String proyectoId);

	long countByProyectoIdAndEstado(String proyectoId, String estado);
}
