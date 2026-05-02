package com.sigpro.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.PlanPruebas;

public interface PlanPruebasRepository extends MongoRepository<PlanPruebas, String> {

	List<PlanPruebas> findByProyectoId(String proyectoId);

	List<PlanPruebas> findByProyectoIdAndSprintId(String proyectoId, String sprintId);
}
