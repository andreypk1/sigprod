package com.sigpro.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.Sprint;

public interface SprintRepository extends MongoRepository<Sprint, String> {

	List<Sprint> findByProyectoId(String proyectoId);

	List<Sprint> findByProyectoIdAndEstado(String proyectoId, String estado);
}
