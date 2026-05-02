package com.sigpro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sigpro.model.Sprint;
import com.sigpro.repository.SprintRepository;
import com.sigpro.service.ProyectoService;
import com.sigpro.service.SprintGestionService;

@Controller
@RequestMapping("/sprints")
public class SprintsWebController {

	private final ProyectoService proyectoService;
	private final SprintRepository sprintRepository;
	private final SprintGestionService sprintGestionService;

	public SprintsWebController(ProyectoService proyectoService, SprintRepository sprintRepository,
			SprintGestionService sprintGestionService) {
		this.proyectoService = proyectoService;
		this.sprintRepository = sprintRepository;
		this.sprintGestionService = sprintGestionService;
	}

	@GetMapping({"", "/"})
	public String index(@RequestParam(required = false) String proyectoId,
			@RequestParam(required = false) String editSprint,
			Model model) {
		model.addAttribute("proyectos", proyectoService.listarFiltrado(null, null, null));
		model.addAttribute("proyectoId", proyectoId);

		Sprint sprintForm = new Sprint();
		if (proyectoId != null && !proyectoId.isBlank()) {
			proyectoService.findById(proyectoId).ifPresent(p -> model.addAttribute("proyecto", p));
			model.addAttribute("sprints", sprintRepository.findByProyectoId(proyectoId));

			if (editSprint != null && !editSprint.isBlank()) {
				sprintForm = sprintGestionService.findById(editSprint).orElse(sprintForm);
			}
			if (sprintForm.getProyectoId() == null || sprintForm.getProyectoId().isBlank()) {
				sprintForm.setProyectoId(proyectoId);
			}
		}

		model.addAttribute("sprint", sprintForm);
		return "modulos/sprints";
	}

	@PostMapping("/guardar")
	public String guardar(Sprint sprint, RedirectAttributes ra) {
		sprintGestionService.guardar(sprint);
		ra.addFlashAttribute("msg", "Sprint guardado.");
		return redirectBack(sprint.getProyectoId());
	}

	@PostMapping("/{id}/eliminar")
	public String eliminar(@PathVariable String id, @RequestParam String proyectoId, RedirectAttributes ra) {
		sprintGestionService.eliminar(id);
		ra.addFlashAttribute("msg", "Sprint eliminado.");
		return redirectBack(proyectoId);
	}

	private static String redirectBack(String proyectoId) {
		if (proyectoId == null || proyectoId.isBlank()) {
			return "redirect:/sprints";
		}
		return "redirect:/sprints?proyectoId=" + proyectoId;
	}
}
