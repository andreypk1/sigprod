package com.sigpro.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.Defecto;

public interface DefectoRepository extends MongoRepository<Defecto, String> {

	List<Defecto> findByProyectoId(String proyectoId);

	long countByProyectoIdAndEstadoNot(String proyectoId, String estado);

	List<Defecto> findByFechaReporteBetween(LocalDateTime inicio, LocalDateTime fin);
}
