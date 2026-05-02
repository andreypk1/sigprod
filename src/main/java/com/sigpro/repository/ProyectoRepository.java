package com.sigpro.repository;

import java.util.List;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.Proyecto;

public interface ProyectoRepository extends MongoRepository<Proyecto, String> {

	boolean existsByNombreIgnoreCase(String nombre);

	Optional<Proyecto> findByNombreIgnoreCase(String nombre);

	List<Proyecto> findByEstado(String estado);

	List<Proyecto> findByNombreContainingIgnoreCase(String nombre);

	List<Proyecto> findByMetodologia(String metodologia);

	List<Proyecto> findByEstadoAndMetodologia(String estado, String metodologia);

	List<Proyecto> findByClienteId(String clienteId);

	List<Proyecto> findByProductOwnerId(String productOwnerId);
}
