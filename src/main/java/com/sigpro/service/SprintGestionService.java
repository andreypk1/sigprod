package com.sigpro.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sigpro.model.Sprint;
import com.sigpro.repository.SprintRepository;

@Service
public class SprintGestionService {

	private final SprintRepository sprintRepository;

	public SprintGestionService(SprintRepository sprintRepository) {
		this.sprintRepository = sprintRepository;
	}

	public Sprint guardar(Sprint s) {
		if (s.getId() != null && s.getId().isBlank()) {
			s.setId(null);
		}
		return sprintRepository.save(s);
	}

	public void eliminar(String id) {
		sprintRepository.deleteById(id);
	}

	public Optional<Sprint> findById(String id) {
		return sprintRepository.findById(id);
	}
}
