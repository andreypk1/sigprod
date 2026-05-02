package com.sigpro.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sigpro.model.Defecto;
import com.sigpro.repository.DefectoRepository;

@Service
public class DefectoService {

	private final DefectoRepository defectoRepository;

	public DefectoService(DefectoRepository defectoRepository) {
		this.defectoRepository = defectoRepository;
	}

	public Defecto guardar(Defecto d) {
		if (d.getId() != null && d.getId().isBlank()) {
			d.setId(null);
		}
		if (d.getFechaReporte() == null) {
			d.setFechaReporte(LocalDateTime.now());
		}
		return defectoRepository.save(d);
	}

	public void eliminar(String id) {
		defectoRepository.deleteById(id);
	}

	public Optional<Defecto> findById(String id) {
		return defectoRepository.findById(id);
	}
}
