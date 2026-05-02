package com.sigpro.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sigpro.model.Defecto;
import com.sigpro.model.HistoriaUsuario;
import com.sigpro.model.Proyecto;
import com.sigpro.model.RolSistema;
import com.sigpro.model.Tarea;
import com.sigpro.model.Usuario;
import com.sigpro.repository.DefectoRepository;
import com.sigpro.repository.HistoriaUsuarioRepository;
import com.sigpro.repository.ProyectoRepository;
import com.sigpro.repository.RiesgoRepository;
import com.sigpro.repository.SprintRepository;
import com.sigpro.repository.TareaRepository;
import com.sigpro.util.EstadosTarea;

@Service
public class DashboardService {

	private final ProyectoRepository proyectoRepository;
	private final SprintRepository sprintRepository;
	private final TareaRepository tareaRepository;
	private final HistoriaUsuarioRepository historiaUsuarioRepository;
	private final DefectoRepository defectoRepository;
	private final RiesgoRepository riesgoRepository;

	public DashboardService(ProyectoRepository proyectoRepository, SprintRepository sprintRepository,
			TareaRepository tareaRepository, HistoriaUsuarioRepository historiaUsuarioRepository,
			DefectoRepository defectoRepository, RiesgoRepository riesgoRepository) {
		this.proyectoRepository = proyectoRepository;
		this.sprintRepository = sprintRepository;
		this.tareaRepository = tareaRepository;
		this.historiaUsuarioRepository = historiaUsuarioRepository;
		this.defectoRepository = defectoRepository;
		this.riesgoRepository = riesgoRepository;
	}

	public Map<String, Object> kpisPorRol(Usuario usuario) {
		RolSistema rol = rolResuelto(usuario);
		Map<String, Object> m = new HashMap<>();
		List<Proyecto> misProyectos = proyectosVisibles(usuario);

		if (rol == RolSistema.ADMINISTRADOR) {
			long activos = proyectoRepository.findAll().stream().filter(p -> "ACTIVO".equals(p.getEstado())).count();
			long sprintsActivos = sprintRepository.findAll().stream().filter(s -> "ACTIVO".equals(s.getEstado())).count();
			long defectosAbiertos = defectoRepository.findAll().stream()
					.filter(d -> !Arrays.asList("CERRADO", "VERIFICADO").contains(d.getEstado())).count();
			m.put("k1", activos);
			m.put("k1Label", "Proyectos activos");
			m.put("k2", sprintsActivos);
			m.put("k2Label", "Sprints en curso");
			m.put("k3", defectosAbiertos);
			m.put("k3Label", "Defectos abiertos");
			m.put("k4", contarRiesgosAbiertos(misProyectos));
			m.put("k4Label", "Riesgos abiertos");
		} else if (rol == RolSistema.PROJECT_MANAGER) {
			long activos = misProyectos.stream().filter(p -> "ACTIVO".equals(p.getEstado())).count();
			long sprintsActivos = sprintRepository.findAll().stream()
					.filter(s -> "ACTIVO".equals(s.getEstado())
							&& misProyectos.stream().anyMatch(pr -> pr.getId().equals(s.getProyectoId())))
					.count();
			double presupuestoConsumido = calcularPresupuestoConsumido(misProyectos);
			long riesgosAbiertos = contarRiesgosAbiertos(misProyectos);
			m.put("k1", activos);
			m.put("k1Label", "Proyectos activos");
			m.put("k2", sprintsActivos);
			m.put("k2Label", "Sprints en curso");
			m.put("k3", presupuestoConsumido);
			m.put("k3Label", "Presupuesto consumido (%)");
			m.put("k4", riesgosAbiertos);
			m.put("k4Label", "Riesgos abiertos");
		} else if (rol == RolSistema.PRODUCT_OWNER) {
			long pendientes = historiaUsuarioRepository.findAll().stream()
					.filter(h -> "EN_REVISION".equals(h.getEstado())).count();
			long sprintsCompletados = sprintRepository.findAll().stream().filter(s -> "COMPLETADO".equals(s.getEstado()))
					.count();
			double avance = avancePromedioHistorias();
			m.put("k1", pendientes);
			m.put("k1Label", "Historias pendientes de aceptación");
			m.put("k2", sprintsCompletados);
			m.put("k2Label", "Sprints completados");
			m.put("k3", avance);
			m.put("k3Label", "Avance backlog (%)");
			m.put("k4", misProyectos.size());
			m.put("k4Label", "Proyectos visibles");
		} else if (rol == RolSistema.QA_ENGINEER) {
			long defectosAbiertos = defectoRepository.findAll().stream()
					.filter(d -> !Arrays.asList("CERRADO", "VERIFICADO").contains(d.getEstado())).count();
			m.put("k1", defectosAbiertos);
			m.put("k1Label", "Defectos abiertos");
			m.put("k2", tasaAceptacionDefectos());
			m.put("k2Label", "Tasa verificación (%)");
			m.put("k3", tareasQACompletadas());
			m.put("k3Label", "Tareas QA completadas");
			m.put("k4", misProyectos.size());
			m.put("k4Label", "Proyectos");
		} else if (rol == RolSistema.DEVOPS_ENGINEER) {
			LocalDateTime inicioMes = LocalDate.now().withDayOfMonth(1).atStartOfDay();
			long desplieguesMes = 0;
			long incidentes = defectoRepository.findAll().stream()
					.filter(d -> d.getFechaReporte() != null && d.getFechaReporte().isAfter(inicioMes)
							&& !"CERRADO".equals(d.getEstado()))
					.count();
			m.put("k1", desplieguesMes);
			m.put("k1Label", "Despliegues del mes");
			m.put("k2", incidentes);
			m.put("k2Label", "Incidentes abiertos");
			m.put("k3", 99.5);
			m.put("k3Label", "Uptime estimado (%)");
			m.put("k4", tareasTipoDevops());
			m.put("k4Label", "Tareas DevOps activas");
		} else {
			long tareasAsignadas = tareaRepository.findByAsignadoId(usuario.getId()).stream()
					.filter(t -> !EstadosTarea.COMPLETADA.equals(t.getEstado())).count();
			double horas = tareaRepository.findByAsignadoId(usuario.getId()).stream()
					.map(Tarea::getHorasReales).filter(h -> h != null).mapToDouble(Double::doubleValue).sum();
			m.put("k1", tareasAsignadas);
			m.put("k1Label", "Tareas asignadas");
			m.put("k2", horas);
			m.put("k2Label", "Horas registradas");
			m.put("k3", misProyectos.size());
			m.put("k3Label", "Proyectos");
			m.put("k4", 0);
			m.put("k4Label", "PRs activos");
		}
		return m;
	}

	private static RolSistema rolResuelto(Usuario usuario) {
		RolSistema rol = usuario.getRol();
		if (rol == null && usuario.getCorreo() != null
				&& "admin@sigprod.com".equalsIgnoreCase(usuario.getCorreo().trim())) {
			return RolSistema.ADMINISTRADOR;
		}
		return rol;
	}

	private List<Proyecto> proyectosVisibles(Usuario usuario) {
		String uid = usuario.getId();
		RolSistema rol = rolResuelto(usuario);
		if (rol == RolSistema.ADMINISTRADOR) {
			return proyectoRepository.findAll();
		}
		return proyectoRepository.findAll().stream()
				.filter(p -> uid.equals(p.getProductOwnerId()) || uid.equals(p.getProjectManagerId())
						|| (p.getMiembros() != null && p.getMiembros().stream().anyMatch(m -> uid.equals(m.getUsuarioId()))))
				.collect(Collectors.toList());
	}

	private double calcularPresupuestoConsumido(List<Proyecto> proyectos) {
		if (proyectos.isEmpty()) {
			return 0;
		}
		double sum = 0;
		int n = 0;
		for (Proyecto p : proyectos) {
			if (p.getPresupuesto() != null && p.getPresupuesto() > 0) {
				double horas = tareaRepository.findByProyectoId(p.getId()).stream().map(Tarea::getHorasReales)
						.filter(h -> h != null).mapToDouble(Double::doubleValue).sum();
				double consumo = Math.min(100, horas * 50 / p.getPresupuesto() * 100);
				sum += consumo;
				n++;
			}
		}
		return n > 0 ? Math.round(sum / n * 10.0) / 10.0 : 0;
	}

	private long contarRiesgosAbiertos(List<Proyecto> proyectos) {
		long c = 0;
		for (Proyecto p : proyectos) {
			c += riesgoRepository.findByProyectoId(p.getId()).stream().filter(r -> !"CERRADO".equals(r.getEstado()))
					.count();
		}
		return c;
	}

	private double avancePromedioHistorias() {
		List<HistoriaUsuario> all = historiaUsuarioRepository.findAll();
		if (all.isEmpty()) {
			return 0;
		}
		long done = all.stream().filter(h -> "ACEPTADA".equals(h.getEstado())).count();
		return Math.round(done * 100.0 / all.size() * 10.0) / 10.0;
	}

	private double tasaAceptacionDefectos() {
		List<Defecto> all = defectoRepository.findAll();
		if (all.isEmpty()) {
			return 100;
		}
		long ver = all.stream().filter(d -> "VERIFICADO".equals(d.getEstado()) || "CERRADO".equals(d.getEstado()))
				.count();
		return Math.round(ver * 100.0 / all.size() * 10.0) / 10.0;
	}

	private long tareasQACompletadas() {
		return tareaRepository.findAll().stream()
				.filter(t -> "QA".equals(t.getTipo()) && EstadosTarea.COMPLETADA.equals(t.getEstado())).count();
	}

	private long tareasTipoDevops() {
		return tareaRepository.findAll().stream().filter(t -> "DEVOPS".equals(t.getTipo())
				&& !EstadosTarea.COMPLETADA.equals(t.getEstado())).count();
	}
}
