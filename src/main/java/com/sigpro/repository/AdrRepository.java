package com.sigpro.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.Adr;

public interface AdrRepository extends MongoRepository<Adr, String> {

	List<Adr> findByProyectoIdOrderByNumeroAsc(String proyectoId);
}
