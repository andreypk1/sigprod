package com.sigpro.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sigpro.model.MiembroProyecto;
import com.sigpro.model.Proyecto;
import com.sigpro.model.RolSistema;
import com.sigpro.model.Tarea;
import com.sigpro.repository.ProyectoRepository;
import com.sigpro.repository.TareaRepository;
import com.sigpro.security.UsuarioPrincipal;

@RestController
@RequestMapping("/api/proyectos")
@PreAuthorize("hasAnyRole('ADMINISTRADOR','PROJECT_MANAGER','PRODUCT_OWNER','ANALISTA_NEGOCIO','ARQUITECTO_SOFTWARE','DISENADOR_UI_UX','DESARROLLADOR_FRONTEND','DESARROLLADOR_BACKEND','QA_ENGINEER','DEVOPS_ENGINEER')")
public class ProyectoApiController {

	private final ProyectoRepository proyectoRepository;
	private final TareaRepository tareaRepository;

	public ProyectoApiController(ProyectoRepository proyectoRepository, TareaRepository tareaRepository) {
		this.proyectoRepository = proyectoRepository;
		this.tareaRepository = tareaRepository;
	}

	@GetMapping
	public List<Proyecto> misProyectos(@AuthenticationPrincipal UsuarioPrincipal principal) {
		String uid = principal.getId();
		RolSistema rol = principal.getRolSistema();
		if (rol == RolSistema.ADMINISTRADOR) {
			return proyectoRepository.findAll();
		}
		return proyectoRepository.findAll().stream().filter(p -> visible(uid, p)).collect(Collectors.toList());
	}

	private boolean visible(String uid, Proyecto p) {
		if (uid.equals(p.getProductOwnerId()) || uid.equals(p.getProjectManagerId())) {
			return true;
		}
		if (p.getMiembros() == null) {
			return false;
		}
		for (MiembroProyecto m : p.getMiembros()) {
			if (uid.equals(m.getUsuarioId())) {
				return true;
			}
		}
		return false;
	}

	@GetMapping("/{id}/miembros")
	public ResponseEntity<List<MiembroProyecto>> miembros(@PathVariable String id,
			@AuthenticationPrincipal UsuarioPrincipal principal) {
		return proyectoRepository.findById(id).filter(p -> visible(principal.getId(), p) || principal.getRolSistema() == RolSistema.ADMINISTRADOR)
				.map(p -> ResponseEntity.ok(p.getMiembros()))
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/{id}/tareas")
	public ResponseEntity<List<Tarea>> tareas(@PathVariable String id,
			@AuthenticationPrincipal UsuarioPrincipal principal) {
		if (proyectoRepository.findById(id).filter(p -> visible(principal.getId(), p) || principal.getRolSistema() == RolSistema.ADMINISTRADOR).isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(tareaRepository.findByProyectoId(id));
	}
}
