package com.sigpro.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.HistoriaUsuario;

public interface HistoriaUsuarioRepository extends MongoRepository<HistoriaUsuario, String> {

	List<HistoriaUsuario> findByProyectoId(String proyectoId);

	List<HistoriaUsuario> findByEpicaId(String epicaId);

	List<HistoriaUsuario> findByProyectoIdAndEstado(String proyectoId, String estado);

	List<HistoriaUsuario> findBySprintId(String sprintId);

	List<HistoriaUsuario> findByProyectoIdInAndEstadoIn(List<String> proyectoIds, List<String> estados);
}
