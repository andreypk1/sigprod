package com.sigpro.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sigpro.model.HistoriaUsuario;
import com.sigpro.repository.HistoriaUsuarioRepository;

@Service
public class HistoriaUsuarioService {

	private final HistoriaUsuarioRepository historiaUsuarioRepository;

	public HistoriaUsuarioService(HistoriaUsuarioRepository historiaUsuarioRepository) {
		this.historiaUsuarioRepository = historiaUsuarioRepository;
	}

	public HistoriaUsuario guardar(HistoriaUsuario h) {
		if (h.getId() != null && h.getId().isBlank()) {
			h.setId(null);
		}
		return historiaUsuarioRepository.save(h);
	}

	public void eliminar(String id) {
		historiaUsuarioRepository.deleteById(id);
	}

	public Optional<HistoriaUsuario> findById(String id) {
		return historiaUsuarioRepository.findById(id);
	}
}
