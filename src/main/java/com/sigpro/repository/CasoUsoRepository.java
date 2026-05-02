package com.sigpro.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.CasoUso;

public interface CasoUsoRepository extends MongoRepository<CasoUso, String> {

	List<CasoUso> findByProyectoId(String proyectoId);
}
