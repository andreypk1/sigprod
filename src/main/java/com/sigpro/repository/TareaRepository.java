package com.sigpro.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.Tarea;

public interface TareaRepository extends MongoRepository<Tarea, String> {

	List<Tarea> findByProyectoId(String proyectoId);

	List<Tarea> findByProyectoIdAndEstado(String proyectoId, String estado);

	List<Tarea> findByAsignadoId(String asignadoId);

	long countByProyectoIdAndEstado(String proyectoId, String estado);

	List<Tarea> findByProyectoIdAndTipo(String proyectoId, String tipo);

	List<Tarea> findBySprintId(String sprintId);
}
