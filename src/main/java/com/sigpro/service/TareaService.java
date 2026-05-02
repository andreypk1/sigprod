package com.sigpro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sigpro.dto.TareaApiDto;
import com.sigpro.model.Tarea;
import com.sigpro.model.Usuario;
import com.sigpro.repository.TareaRepository;
import com.sigpro.repository.UsuarioRepository;
import com.sigpro.util.EstadosTarea;

@Service
public class TareaService {

	private final TareaRepository tareaRepository;
	private final UsuarioRepository usuarioRepository;

	public TareaService(TareaRepository tareaRepository, UsuarioRepository usuarioRepository) {
		this.tareaRepository = tareaRepository;
		this.usuarioRepository = usuarioRepository;
	}

	public List<Tarea> listarPorProyecto(String proyectoId) {
		return tareaRepository.findByProyectoId(proyectoId);
	}

	public Optional<Tarea> findById(String id) {
		return tareaRepository.findById(id);
	}

	public Tarea crear(TareaApiDto dto) {
		Tarea t = new Tarea();
		aplicarDto(t, dto);
		if (t.getEstado() == null || t.getEstado().isBlank()) {
			t.setEstado(EstadosTarea.TODO);
		}
		resolverAsignado(t);
		return tareaRepository.save(t);
	}

	public Tarea actualizarEstado(String id, String nuevoEstado) {
		Tarea t = tareaRepository.findById(id).orElseThrow(() -> new BusinessRuleException("Tarea no encontrada."));
		if (EstadosTarea.COMPLETADA.equals(nuevoEstado)) {
			Double reales = t.getHorasReales();
			if (reales == null || reales <= 0) {
				throw new BusinessRuleException(
						"Solo se puede marcar como COMPLETADA una tarea con horas reales registradas mayor a 0.");
			}
		}
		t.setEstado(nuevoEstado);
		return tareaRepository.save(t);
	}

	private void aplicarDto(Tarea t, TareaApiDto dto) {
		t.setTitulo(dto.getTitulo());
		t.setDescripcion(dto.getDescripcion());
		t.setHistoriaId(dto.getHistoriaId());
		t.setProyectoId(dto.getProyectoId());
		t.setSprintId(dto.getSprintId());
		t.setTipo(dto.getTipo());
		t.setAsignadoId(dto.getAsignadoId());
		t.setHorasEstimadas(dto.getHorasEstimadas());
		t.setHorasReales(dto.getHorasReales());
		t.setEstado(dto.getEstado());
		t.setPrioridad(dto.getPrioridad());
		t.setFechaLimite(dto.getFechaLimite());
	}

	private void resolverAsignado(Tarea t) {
		if (t.getAsignadoId() != null && !t.getAsignadoId().isBlank()) {
			Usuario u = usuarioRepository.findById(t.getAsignadoId()).orElse(null);
			if (u != null) {
				t.setAsignadoNombre(u.nombreCompleto().trim());
			}
		}
	}

	public Tarea actualizar(String id, TareaApiDto dto) {
		Tarea t = tareaRepository.findById(id).orElseThrow(() -> new BusinessRuleException("Tarea no encontrada."));
		aplicarDto(t, dto);
		resolverAsignado(t);
		return tareaRepository.save(t);
	}

	public void eliminar(String id) {
		if (!tareaRepository.existsById(id)) {
			throw new BusinessRuleException("Tarea no encontrada.");
		}
		tareaRepository.deleteById(id);
	}

	public List<Tarea> listarPorProyectoYTipo(String proyectoId, String tipo) {
		return tareaRepository.findByProyectoIdAndTipo(proyectoId, tipo);
	}
}
