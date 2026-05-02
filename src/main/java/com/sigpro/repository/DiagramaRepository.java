package com.sigpro.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.Diagrama;

public interface DiagramaRepository extends MongoRepository<Diagrama, String> {

	List<Diagrama> findByProyectoId(String proyectoId);

	List<Diagrama> findByProyectoIdAndTipo(String proyectoId, String tipo);
}
