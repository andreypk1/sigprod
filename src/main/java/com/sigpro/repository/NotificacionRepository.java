package com.sigpro.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sigpro.model.Notificacion;

public interface NotificacionRepository extends MongoRepository<Notificacion, String> {

	List<Notificacion> findTop5ByUsuarioDestinoIdAndLeidaFalseOrderByFechaDesc(String usuarioDestinoId);

	List<Notificacion> findByUsuarioDestinoIdOrderByFechaDesc(String usuarioDestinoId);

	long countByUsuarioDestinoIdAndLeidaFalse(String usuarioDestinoId);
}
