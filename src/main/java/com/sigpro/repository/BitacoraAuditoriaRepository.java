package com.sigpro.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.BitacoraAuditoria;

public interface BitacoraAuditoriaRepository extends MongoRepository<BitacoraAuditoria, String> {

	List<BitacoraAuditoria> findByUsuarioId(String usuarioId);

	List<BitacoraAuditoria> findByAccion(String accion);

	List<BitacoraAuditoria> findByEntidad(String entidad);

	List<BitacoraAuditoria> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}
