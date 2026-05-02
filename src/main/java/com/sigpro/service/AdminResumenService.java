package com.sigpro.service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sigpro.model.Epica;
import com.sigpro.model.HistoriaUsuario;
import com.sigpro.model.Proyecto;
import com.sigpro.repository.EpicaRepository;
import com.sigpro.repository.HistoriaUsuarioRepository;
import com.sigpro.repository.ProyectoRepository;

@Service
public class AdminResumenService {

	private final ProyectoRepository proyectoRepository;
	private final HistoriaUsuarioRepository historiaUsuarioRepository;
	private final EpicaRepository epicaRepository;

	public AdminResumenService(ProyectoRepository proyectoRepository,
			HistoriaUsuarioRepository historiaUsuarioRepository, EpicaRepository epicaRepository) {
		this.proyectoRepository = proyectoRepository;
		this.historiaUsuarioRepository = historiaUsuarioRepository;
		this.epicaRepository = epicaRepository;
	}

	public ConteosGlobales conteosGlobales() {
		List<Proyecto> proyectos = proyectoRepository.findAll();
		long activos = proyectos.stream().filter(p -> "ACTIVO".equals(p.getEstado())).count();
		List<HistoriaUsuario> hu = historiaUsuarioRepository.findAll();
		long totalHu = hu.size();
		long epicas = epicaRepository.count();
		long huEstadoBacklog = hu.stream().filter(h -> "BACKLOG".equals(h.getEstado())).count();
		return new ConteosGlobales(activos, (long) proyectos.size(), totalHu, epicas, huEstadoBacklog);
	}

	public Map<String, Long> historiasPorEstado() {
		return historiaUsuarioRepository.findAll().stream()
				.collect(Collectors.groupingBy(h -> Optional.ofNullable(h.getEstado()).filter(s -> !s.isBlank()).orElse("SIN_ESTADO"),
						Collectors.counting())).entrySet().stream()
				.sorted(Map.Entry.<String, Long>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
	}

	public List<FilaProyectoBacklog> filasPorProyecto() {
		List<Proyecto> proyectos = proyectoRepository.findAll();
		Map<String, Long> huPorProyecto = historiaUsuarioRepository.findAll().stream()
				.filter(h -> h.getProyectoId() != null)
				.collect(Collectors.groupingBy(HistoriaUsuario::getProyectoId, Collectors.counting()));
		Map<String, Long> epPorProyecto = epicaRepository.findAll().stream()
				.filter(e -> e.getProyectoId() != null)
				.collect(Collectors.groupingBy(Epica::getProyectoId, Collectors.counting()));
		return proyectos.stream()
				.map(p -> new FilaProyectoBacklog(
						p.getId(),
						p.getNombre(),
						p.getClienteNombre(),
						p.getEstado(),
						epPorProyecto.getOrDefault(p.getId(), 0L),
						huPorProyecto.getOrDefault(p.getId(), 0L)))
				.sorted(Comparator.comparing(FilaProyectoBacklog::getNombre, String.CASE_INSENSITIVE_ORDER))
				.toList();
	}

	public static final class ConteosGlobales {
		private final long proyectosActivos;
		private final long proyectosTotal;
		private final long historiasTotal;
		private final long epicasTotal;
		private final long historiasEstadoBacklog;

		public ConteosGlobales(long proyectosActivos, long proyectosTotal, long historiasTotal, long epicasTotal,
				long historiasEstadoBacklog) {
			this.proyectosActivos = proyectosActivos;
			this.proyectosTotal = proyectosTotal;
			this.historiasTotal = historiasTotal;
			this.epicasTotal = epicasTotal;
			this.historiasEstadoBacklog = historiasEstadoBacklog;
		}

		public long getProyectosActivos() {
			return proyectosActivos;
		}

		public long getProyectosTotal() {
			return proyectosTotal;
		}

		public long getHistoriasTotal() {
			return historiasTotal;
		}

		public long getEpicasTotal() {
			return epicasTotal;
		}

		public long getHistoriasEstadoBacklog() {
			return historiasEstadoBacklog;
		}
	}

	public static final class FilaProyectoBacklog {
		private final String proyectoId;
		private final String nombre;
		private final String clienteNombre;
		private final String estadoProyecto;
		private final long epicas;
		private final long historias;

		public FilaProyectoBacklog(String proyectoId, String nombre, String clienteNombre, String estadoProyecto,
				long epicas, long historias) {
			this.proyectoId = proyectoId;
			this.nombre = nombre;
			this.clienteNombre = clienteNombre;
			this.estadoProyecto = estadoProyecto;
			this.epicas = epicas;
			this.historias = historias;
		}

		public String getProyectoId() {
			return proyectoId;
		}

		public String getNombre() {
			return nombre;
		}

		public String getClienteNombre() {
			return clienteNombre;
		}

		public String getEstadoProyecto() {
			return estadoProyecto;
		}

		public long getEpicas() {
			return epicas;
		}

		public long getHistorias() {
			return historias;
		}
	}
}
