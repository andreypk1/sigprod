package com.sigpro.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.EndpointApi;

public interface EndpointApiRepository extends MongoRepository<EndpointApi, String> {

	List<EndpointApi> findByProyectoId(String proyectoId);
}
