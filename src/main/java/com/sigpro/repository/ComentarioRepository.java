package com.sigpro.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.Comentario;

public interface ComentarioRepository extends MongoRepository<Comentario, String> {

	List<Comentario> findByEntidadTipoAndEntidadIdOrderByFechaAsc(String tipo, String id);
}
