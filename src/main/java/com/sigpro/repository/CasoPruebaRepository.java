package com.sigpro.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.CasoPrueba;

public interface CasoPruebaRepository extends MongoRepository<CasoPrueba, String> {

	List<CasoPrueba> findByPlanId(String planId);

	List<CasoPrueba> findByProyectoId(String proyectoId);

	long countByProyectoIdAndEstado(String proyectoId, String estado);
}
