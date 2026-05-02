package com.sigpro.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sigpro.model.Notificacion;
import com.sigpro.repository.NotificacionRepository;

@Service
public class NotificacionService {

	private final NotificacionRepository notificacionRepository;

	public NotificacionService(NotificacionRepository notificacionRepository) {
		this.notificacionRepository = notificacionRepository;
	}

	public List<Notificacion> ultimasNoLeidas(String usuarioId) {
		return notificacionRepository.findTop5ByUsuarioDestinoIdAndLeidaFalseOrderByFechaDesc(usuarioId);
	}

	public long contarNoLeidas(String usuarioId) {
		return notificacionRepository.countByUsuarioDestinoIdAndLeidaFalse(usuarioId);
	}

	public Notificacion marcarLeida(String id, String usuarioId) {
		Notificacion n = notificacionRepository.findById(id).orElseThrow(() -> new BusinessRuleException("Notificación no encontrada."));
		if (!usuarioId.equals(n.getUsuarioDestinoId())) {
			throw new BusinessRuleException("No autorizado.");
		}
		n.setLeida(true);
		return notificacionRepository.save(n);
	}
}
