package com.sigpro.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.EjecucionPipeline;

public interface EjecucionPipelineRepository extends MongoRepository<EjecucionPipeline, String> {

	List<EjecucionPipeline> findByPipelineIdOrderByFechaInicioDesc(String pipelineId);
}
