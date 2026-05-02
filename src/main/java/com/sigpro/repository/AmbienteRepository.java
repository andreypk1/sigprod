package com.sigpro.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.Ambiente;

public interface AmbienteRepository extends MongoRepository<Ambiente, String> {

	List<Ambiente> findByProyectoId(String proyectoId);
}
