package com.sigpro.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.Despliegue;

public interface DespliegueRepository extends MongoRepository<Despliegue, String> {

	List<Despliegue> findByProyectoIdOrderByFechaDesc(String proyectoId);
}
