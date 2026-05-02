package com.sigpro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.RolSistema;
import com.sigpro.model.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {

	Optional<Usuario> findByCorreoIgnoreCase(String correo);

	boolean existsByCorreoIgnoreCase(String correo);

	List<Usuario> findByRol(RolSistema rol);

	List<Usuario> findByEstado(String estado);
}
