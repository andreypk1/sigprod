package com.sigpro.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.Requisito;

public interface RequisitoRepository extends MongoRepository<Requisito, String> {

	List<Requisito> findByProyectoId(String proyectoId);

	List<Requisito> findByProyectoIdAndTipo(String proyectoId, String tipo);
}
