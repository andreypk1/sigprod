package com.sigpro.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sigpro.dto.TareaApiDto;
import com.sigpro.model.Proyecto;
import com.sigpro.model.Tarea;
import com.sigpro.repository.HistoriaUsuarioRepository;
import com.sigpro.repository.SprintRepository;
import com.sigpro.service.BusinessRuleException;
import com.sigpro.service.ProyectoService;
import com.sigpro.service.TareaService;
import com.sigpro.util.EstadosTarea;

@Controller
@RequestMapping("/tareas")
public class TareasGlobalesWebController {

	private final ProyectoService proyectoService;
	private final TareaService tareaService;
	private final HistoriaUsuarioRepository historiaUsuarioRepository;
	private final SprintRepository sprintRepository;

	public TareasGlobalesWebController(ProyectoService proyectoService, TareaService tareaService,
			HistoriaUsuarioRepository historiaUsuarioRepository, SprintRepository sprintRepository) {
		this.proyectoService = proyectoService;
		this.tareaService = tareaService;
		this.historiaUsuarioRepository = historiaUsuarioRepository;
		this.sprintRepository = sprintRepository;
	}

	@GetMapping({"", "/"})
	public String index(@RequestParam(required = false) String proyectoId,
			@RequestParam(required = false) String editTarea,
			Model model) {
		List<Proyecto> proyectos = proyectoService.listarFiltrado(null, null, null);
		model.addAttribute("proyectos", proyectos);
		model.addAttribute("proyectoId", proyectoId);

		TareaApiDto tareaDto = new TareaApiDto();
		if (proyectoId != null && !proyectoId.isBlank()) {
			proyectoService.findById(proyectoId).ifPresent(p -> model.addAttribute("proyecto", p));
			model.addAttribute("tareas", tareaService.listarPorProyecto(proyectoId));
			model.addAttribute("historiasSelect", historiaUsuarioRepository.findByProyectoId(proyectoId));
			model.addAttribute("sprintsSelect", sprintRepository.findByProyectoId(proyectoId));
			model.addAttribute("estados", EstadosTarea.columnasKanban());
			model.addAttribute("tiposTarea", List.of("FRONTEND", "BACKEND", "QA", "DEVOPS", "ARQUITECTURA", "DISENO"));

			if (editTarea != null && !editTarea.isBlank()) {
				tareaService.findById(editTarea).ifPresent(t -> {
					tareaDto.setTitulo(t.getTitulo());
					tareaDto.setDescripcion(t.getDescripcion());
					tareaDto.setHistoriaId(t.getHistoriaId());
					tareaDto.setProyectoId(t.getProyectoId());
					tareaDto.setSprintId(t.getSprintId());
					tareaDto.setTipo(t.getTipo());
					tareaDto.setAsignadoId(t.getAsignadoId());
					tareaDto.setHorasEstimadas(t.getHorasEstimadas());
					tareaDto.setHorasReales(t.getHorasReales());
					tareaDto.setEstado(t.getEstado());
					tareaDto.setPrioridad(t.getPrioridad());
					tareaDto.setFechaLimite(t.getFechaLimite());
					model.addAttribute("tareaEditId", t.getId());
				});
			}
			tareaDto.setProyectoId(proyectoId);
		}

		model.addAttribute("tareaDto", tareaDto);
		return "modulos/tareas";
	}

	@PostMapping("/guardar")
	public String guardar(@RequestParam(required = false) String tareaEditId, TareaApiDto dto, RedirectAttributes ra) {
		try {
			if (tareaEditId != null && !tareaEditId.isBlank()) {
				tareaService.actualizar(tareaEditId, dto);
				ra.addFlashAttribute("msg", "Tarea actualizada.");
			} else {
				tareaService.crear(dto);
				ra.addFlashAttribute("msg", "Tarea creada.");
			}
		} catch (BusinessRuleException ex) {
			ra.addFlashAttribute("msgError", ex.getMessage());
		}
		return redirectBack(dto.getProyectoId());
	}

	@PostMapping("/{id}/eliminar")
	public String eliminar(@PathVariable String id, @RequestParam String proyectoId, RedirectAttributes ra) {
		try {
			tareaService.eliminar(id);
			ra.addFlashAttribute("msg", "Tarea eliminada.");
		} catch (BusinessRuleException ex) {
			ra.addFlashAttribute("msgError", ex.getMessage());
		}
		return redirectBack(proyectoId);
	}

	private static String redirectBack(String proyectoId) {
		if (proyectoId == null || proyectoId.isBlank()) {
			return "redirect:/tareas";
		}
		return "redirect:/tareas?proyectoId=" + proyectoId;
	}
}
