package com.sigpro.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sigpro.model.HistoriaUsuario;
import com.sigpro.model.Proyecto;
import com.sigpro.model.Tarea;
import com.sigpro.model.Usuario;
import com.sigpro.repository.HistoriaUsuarioRepository;
import com.sigpro.repository.UsuarioRepository;
import com.sigpro.service.ProyectoService;
import com.sigpro.service.TareaService;
import com.sigpro.util.EstadosTarea;

@Controller
@RequestMapping("/kanban")
public class KanbanController {

	private final ProyectoService proyectoService;
	private final TareaService tareaService;
	private final HistoriaUsuarioRepository historiaUsuarioRepository;
	private final UsuarioRepository usuarioRepository;

	public KanbanController(ProyectoService proyectoService, TareaService tareaService,
			HistoriaUsuarioRepository historiaUsuarioRepository, UsuarioRepository usuarioRepository) {
		this.proyectoService = proyectoService;
		this.tareaService = tareaService;
		this.historiaUsuarioRepository = historiaUsuarioRepository;
		this.usuarioRepository = usuarioRepository;
	}

	@GetMapping("/{proyectoId}")
	public String tablero(@PathVariable String proyectoId, Model model) {
		Proyecto proyecto = proyectoService.findById(proyectoId).orElseThrow();
		List<Tarea> tareas = tareaService.listarPorProyecto(proyectoId);
		List<HistoriaUsuario> historias = historiaUsuarioRepository.findByProyectoId(proyectoId);
		List<Usuario> miembros = usuarioRepository.findAll().stream()
				.filter(u -> proyecto.getMiembros() != null
						&& proyecto.getMiembros().stream().anyMatch(m -> m.getUsuarioId().equals(u.getId())))
				.toList();
		if (miembros.isEmpty()) {
			miembros = usuarioRepository.findAll();
		}

		model.addAttribute("proyecto", proyecto);
		model.addAttribute("tareas", tareas);
		model.addAttribute("historias", historias);
		model.addAttribute("miembrosProyecto", miembros);
		model.addAttribute("columnas", EstadosTarea.columnasKanban());
		return "kanban/tablero";
	}
}
