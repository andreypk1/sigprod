package com.sigpro.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sigpro.dto.MiembroProyectoDto;
import com.sigpro.dto.ProyectoFormDto;
import com.sigpro.model.Cliente;
import com.sigpro.model.MiembroProyecto;
import com.sigpro.model.Proyecto;
import com.sigpro.model.RolSistema;
import com.sigpro.model.Tarea;
import com.sigpro.model.Usuario;
import com.sigpro.repository.ClienteRepository;
import com.sigpro.repository.ProyectoRepository;
import com.sigpro.repository.TareaRepository;
import com.sigpro.repository.UsuarioRepository;
import com.sigpro.util.EstadosTarea;

@Service
public class ProyectoService {

	private final ProyectoRepository proyectoRepository;
	private final ClienteRepository clienteRepository;
	private final UsuarioRepository usuarioRepository;
	private final TareaRepository tareaRepository;

	public ProyectoService(ProyectoRepository proyectoRepository, ClienteRepository clienteRepository,
			UsuarioRepository usuarioRepository, TareaRepository tareaRepository) {
		this.proyectoRepository = proyectoRepository;
		this.clienteRepository = clienteRepository;
		this.usuarioRepository = usuarioRepository;
		this.tareaRepository = tareaRepository;
	}

	public List<Proyecto> listarFiltrado(String estado, String metodologia, String nombre) {
		List<Proyecto> todos = proyectoRepository.findAll();
		return todos.stream()
				.filter(p -> estado == null || estado.isBlank() || estado.equals(p.getEstado()))
				.filter(p -> metodologia == null || metodologia.isBlank() || metodologia.equals(p.getMetodologia()))
				.filter(p -> nombre == null || nombre.isBlank()
						|| (p.getNombre() != null && p.getNombre().toLowerCase().contains(nombre.toLowerCase())))
				.collect(Collectors.toList());
	}

	public Optional<Proyecto> findById(String id) {
		return proyectoRepository.findById(id);
	}

	public Proyecto crear(ProyectoFormDto dto) {
		validarRolesEquipo(dto.getProductOwnerId(), dto.getProjectManagerId());
		validarMiembrosUnicos(dto.getMiembros());

		Cliente cliente = clienteRepository.findById(dto.getClienteId())
				.orElseThrow(() -> new BusinessRuleException("Cliente no encontrado."));

		Usuario po = usuarioRepository.findById(dto.getProductOwnerId())
				.orElseThrow(() -> new BusinessRuleException("Product Owner no encontrado."));
		if (po.getRol() != RolSistema.PRODUCT_OWNER) {
			throw new BusinessRuleException("El usuario seleccionado como Product Owner debe tener rol PRODUCT_OWNER en el sistema.");
		}

		Usuario pm = usuarioRepository.findById(dto.getProjectManagerId())
				.orElseThrow(() -> new BusinessRuleException("Project Manager no encontrado."));
		if (pm.getRol() != RolSistema.PROJECT_MANAGER) {
			throw new BusinessRuleException("El usuario seleccionado como Project Manager debe tener rol PROJECT_MANAGER en el sistema.");
		}

		Proyecto p = new Proyecto();
		p.setNombre(dto.getNombre());
		p.setDescripcion(dto.getDescripcion());
		p.setClienteId(cliente.getId());
		p.setClienteNombre(cliente.getRazonSocial());
		p.setMetodologia(dto.getMetodologia());
		p.setFechaInicio(dto.getFechaInicio());
		p.setFechaFin(dto.getFechaFin());
		p.setPresupuesto(dto.getPresupuesto());
		p.setEstado("ACTIVO");

		p.setProductOwnerId(po.getId());
		p.setProductOwnerNombre(po.nombreCompleto().trim());
		p.setProjectManagerId(pm.getId());
		p.setProjectManagerNombre(pm.nombreCompleto().trim());

		for (MiembroProyectoDto md : dto.getMiembros()) {
			if (md.getUsuarioId() == null || md.getUsuarioId().isBlank()) {
				continue;
			}
			Usuario u = usuarioRepository.findById(md.getUsuarioId())
					.orElseThrow(() -> new BusinessRuleException("Miembro no encontrado: " + md.getUsuarioId()));
			MiembroProyecto m = new MiembroProyecto();
			m.setUsuarioId(u.getId());
			m.setUsuarioNombre(u.nombreCompleto().trim());
			m.setCorreo(u.getCorreo());
			m.setRolEnProyecto(md.getRolEnProyecto() != null ? md.getRolEnProyecto() : u.getRol());
			m.setPorcentajeDedicacion(md.getPorcentajeDedicacion() != null ? md.getPorcentajeDedicacion() : 0);
			m.setTarifaHora(md.getTarifaHora());
			m.setFechaAsignacion(LocalDate.now());
			p.getMiembros().add(m);
		}

		Proyecto guardado = proyectoRepository.save(p);
		return guardado;
	}

	private void validarRolesEquipo(String productOwnerId, String projectManagerId) {
		if (productOwnerId == null || productOwnerId.isBlank()) {
			throw new BusinessRuleException("Debe seleccionar un Product Owner.");
		}
		if (projectManagerId == null || projectManagerId.isBlank()) {
			throw new BusinessRuleException("Debe seleccionar un Project Manager.");
		}
	}

	private void validarMiembrosUnicos(List<MiembroProyectoDto> miembros) {
		Set<String> ids = new HashSet<>();
		for (MiembroProyectoDto m : miembros) {
			if (m.getUsuarioId() == null || m.getUsuarioId().isBlank()) {
				continue;
			}
			if (!ids.add(m.getUsuarioId())) {
				throw new BusinessRuleException("Un usuario solo puede agregarse una vez como miembro del proyecto.");
			}
		}
	}

	public double calcularProgreso(String proyectoId) {
		Proyecto p = proyectoRepository.findById(proyectoId).orElse(null);
		if (p != null && p.getProgresoPorcentaje() != null) {
			return p.getProgresoPorcentaje();
		}
		List<Tarea> tareas = tareaRepository.findByProyectoId(proyectoId);
		if (tareas.isEmpty()) {
			return 0;
		}
		long completadas = tareas.stream().filter(t -> EstadosTarea.COMPLETADA.equals(t.getEstado())).count();
		return Math.round((completadas * 100.0 / tareas.size()) * 10.0) / 10.0;
	}
}
