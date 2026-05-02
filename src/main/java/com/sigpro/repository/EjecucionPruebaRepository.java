package com.sigpro.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.EjecucionPrueba;

public interface EjecucionPruebaRepository extends MongoRepository<EjecucionPrueba, String> {

	List<EjecucionPrueba> findByCasoPruebaId(String casoPruebaId);

	List<EjecucionPrueba> findByProyectoId(String proyectoId);

	long countByProyectoIdAndResultado(String proyectoId, String resultado);
}
