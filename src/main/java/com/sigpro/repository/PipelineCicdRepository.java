package com.sigpro.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.PipelineCicd;

public interface PipelineCicdRepository extends MongoRepository<PipelineCicd, String> {

	List<PipelineCicd> findByProyectoId(String proyectoId);
}
