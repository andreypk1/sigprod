package com.sigpro.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.Cliente;

public interface ClienteRepository extends MongoRepository<Cliente, String> {

	Optional<Cliente> findByNitIgnoreCase(String nit);
}
