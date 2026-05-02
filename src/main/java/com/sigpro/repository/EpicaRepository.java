package com.sigpro.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.Epica;

public interface EpicaRepository extends MongoRepository<Epica, String> {

	List<Epica> findByProyectoId(String proyectoId);
}
