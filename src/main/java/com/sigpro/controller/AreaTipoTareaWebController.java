package com.sigpro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sigpro.dto.TareaApiDto;
import com.sigpro.model.Tarea;
import com.sigpro.repository.HistoriaUsuarioRepository;
import com.sigpro.repository.SprintRepository;
import com.sigpro.service.BusinessRuleException;
import com.sigpro.service.ProyectoService;
import com.sigpro.service.TareaService;
import com.sigpro.util.EstadosTarea;
import com.sigpro.util.TipoTareaArea;

@Controller
public class AreaTipoTareaWebController {

	private final ProyectoService proyectoService;
	private final TareaService tareaService;
	private final HistoriaUsuarioRepository historiaUsuarioRepository;
	private final SprintRepository sprintRepository;

	public AreaTipoTareaWebController(ProyectoService proyectoService, TareaService tareaService,
			HistoriaUsuarioRepository historiaUsuarioRepository, SprintRepository sprintRepository) {
		this.proyectoService = proyectoService;
		this.tareaService = tareaService;
		this.historiaUsuarioRepository = historiaUsuarioRepository;
		this.sprintRepository = sprintRepository;
	}

	@GetMapping("/area/arquitectura")
	public String arquitectura(@RequestParam(required = false) String proyectoId,
			@RequestParam(required = false) String editTarea,
			Model model) {
		return vistaArea(model, proyectoId, editTarea, TipoTareaArea.ARQUITECTURA, "Arquitectura", "area/arquitectura");
	}

	@GetMapping("/diseno")
	public String diseno(@RequestParam(required = false) String proyectoId,
			@RequestParam(required = false) String editTarea,
			Model model) {
		return vistaArea(model, proyectoId, editTarea, TipoTareaArea.DISENO, "Diseño", "diseno");
	}

	@GetMapping("/area/devops")
	public String devops(@RequestParam(required = false) String proyectoId,
			@RequestParam(required = false) String editTarea,
			Model model) {
		return vistaArea(model, proyectoId, editTarea, TipoTareaArea.DEVOPS, "DevOps", "area/devops");
	}

	@PostMapping("/area/arquitectura/guardar")
	public String guardarArq(@RequestParam(required = false) String tareaEditId, TareaApiDto dto, RedirectAttributes ra) {
		return guardarConTipo(tareaEditId, dto, TipoTareaArea.ARQUITECTURA, ra, "area/arquitectura");
	}

	@PostMapping("/area/arquitectura/tarea/{id}/eliminar")
	public String eliminarArq(@PathVariable String id, @RequestParam String proyectoId, RedirectAttributes ra) {
		return eliminarConTipo(id, proyectoId, ra, "area/arquitectura");
	}

	@PostMapping("/diseno/guardar")
	public String guardarDis(@RequestParam(required = false) String tareaEditId, TareaApiDto dto, RedirectAttributes ra) {
		return guardarConTipo(tareaEditId, dto, TipoTareaArea.DISENO, ra, "diseno");
	}

	@PostMapping("/diseno/tarea/{id}/eliminar")
	public String eliminarDis(@PathVariable String id, @RequestParam String proyectoId, RedirectAttributes ra) {
		return eliminarConTipo(id, proyectoId, ra, "diseno");
	}

	@PostMapping("/area/devops/guardar")
	public String guardarDev(@RequestParam(required = false) String tareaEditId, TareaApiDto dto, RedirectAttributes ra) {
		return guardarConTipo(tareaEditId, dto, TipoTareaArea.DEVOPS, ra, "area/devops");
	}

	@PostMapping("/area/devops/tarea/{id}/eliminar")
	public String eliminarDev(@PathVariable String id, @RequestParam String proyectoId, RedirectAttributes ra) {
		return eliminarConTipo(id, proyectoId, ra, "area/devops");
	}

	private String vistaArea(Model model, String proyectoId, String editTarea, String tipo, String areaNombre,
			String areaSegment) {
		model.addAttribute("proyectos", proyectoService.listarFiltrado(null, null, null));
		model.addAttribute("proyectoId", proyectoId);
		model.addAttribute("areaNombre", areaNombre);
		model.addAttribute("tipoFijo", tipo);
		model.addAttribute("areaSegment", areaSegment);

		TareaApiDto tareaDto = new TareaApiDto();
		if (proyectoId != null && !proyectoId.isBlank()) {
			proyectoService.findById(proyectoId).ifPresent(p -> model.addAttribute("proyecto", p));
			model.addAttribute("tareas", tareaService.listarPorProyectoYTipo(proyectoId, tipo));
			model.addAttribute("historiasSelect", historiaUsuarioRepository.findByProyectoId(proyectoId));
			model.addAttribute("sprintsSelect", sprintRepository.findByProyectoId(proyectoId));
			model.addAttribute("estados", EstadosTarea.columnasKanban());

			if (editTarea != null && !editTarea.isBlank()) {
				tareaService.findById(editTarea).ifPresent(t -> rellenarDto(model, tareaDto, t));
			}
			tareaDto.setProyectoId(proyectoId);
			tareaDto.setTipo(tipo);
		} else {
			tareaDto.setTipo(tipo);
		}

		model.addAttribute("tareaDto", tareaDto);
		return "modulos/area-tipo-tarea";
	}

	private static void rellenarDto(Model model, TareaApiDto tareaDto, Tarea t) {
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
	}

	private String guardarConTipo(String tareaEditId, TareaApiDto dto, String tipo, RedirectAttributes ra,
			String pathSegment) {
		dto.setTipo(tipo);
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
		return redirectTipo(pathSegment, dto.getProyectoId());
	}

	private String eliminarConTipo(String id, String proyectoId, RedirectAttributes ra, String pathSegment) {
		try {
			tareaService.eliminar(id);
			ra.addFlashAttribute("msg", "Tarea eliminada.");
		} catch (BusinessRuleException ex) {
			ra.addFlashAttribute("msgError", ex.getMessage());
		}
		return redirectTipo(pathSegment, proyectoId);
	}

	private static String redirectTipo(String pathSegment, String proyectoId) {
		if (proyectoId == null || proyectoId.isBlank()) {
			return "redirect:/" + pathSegment;
		}
		return "redirect:/" + pathSegment + "?proyectoId=" + proyectoId;
	}
}
