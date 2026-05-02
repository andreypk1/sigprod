package com.sigpro.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sigpro.model.Epica;
import com.sigpro.repository.EpicaRepository;

@Service
public class EpicaService {

	private final EpicaRepository epicaRepository;

	public EpicaService(EpicaRepository epicaRepository) {
		this.epicaRepository = epicaRepository;
	}

	public Epica guardar(Epica e) {
		if (e.getId() != null && e.getId().isBlank()) {
			e.setId(null);
		}
		return epicaRepository.save(e);
	}

	public void eliminar(String id) {
		epicaRepository.deleteById(id);
	}

	public Optional<Epica> findById(String id) {
		return epicaRepository.findById(id);
	}
}
