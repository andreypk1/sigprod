package com.sigpro.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sigpro.model.CasoUso;
import com.sigpro.model.Diagrama;
import com.sigpro.model.Requisito;
import com.sigpro.repository.CasoUsoRepository;
import com.sigpro.repository.DiagramaRepository;
import com.sigpro.repository.ProyectoRepository;
import com.sigpro.repository.RequisitoRepository;
import com.sigpro.security.UsuarioPrincipal;

@Controller
@RequestMapping("/analista")
@PreAuthorize("hasAnyRole('ADMINISTRADOR','ANALISTA_NEGOCIO','PROJECT_MANAGER')")
public class AnalistaWebController {

	private final RequisitoRepository requisitoRepository;
	private final CasoUsoRepository casoUsoRepository;
	private final DiagramaRepository diagramaRepository;
	private final ProyectoRepository proyectoRepository;

	public AnalistaWebController(RequisitoRepository requisitoRepository, CasoUsoRepository casoUsoRepository,
			DiagramaRepository diagramaRepository, ProyectoRepository proyectoRepository) {
		this.requisitoRepository = requisitoRepository;
		this.casoUsoRepository = casoUsoRepository;
		this.diagramaRepository = diagramaRepository;
		this.proyectoRepository = proyectoRepository;
	}

	@GetMapping({"", "/"})
	public String index(Model model) {
		model.addAttribute("proyectos", proyectoRepository.findAll());
		return "modulos/analista/index";
	}

	@GetMapping("/{proyectoId}/requisitos")
	public String requisitos(@PathVariable String proyectoId, Model model) {
		model.addAttribute("requisitos", requisitoRepository.findByProyectoId(proyectoId));
		model.addAttribute("proyecto", proyectoRepository.findById(proyectoId).orElseThrow());
		return "modulos/analista/requisitos";
	}

	@PostMapping("/{proyectoId}/requisitos")
	public String guardarRequisito(@PathVariable String proyectoId, @ModelAttribute Requisito req,
			@AuthenticationPrincipal UsuarioPrincipal principal, RedirectAttributes ra) {
		req.setProyectoId(proyectoId);
		req.setAnalistaId(principal.getId());
		if (req.getId() == null || req.getId().isBlank()) {
			req.setId(null);
		}
		requisitoRepository.save(req);
		ra.addFlashAttribute("ok", "Requisito guardado.");
		return "redirect:/analista/" + proyectoId + "/requisitos";
	}

	@GetMapping("/{proyectoId}/requisitos/{id}/eliminar")
	public String eliminarRequisito(@PathVariable String proyectoId, @PathVariable String id, RedirectAttributes ra) {
		requisitoRepository.deleteById(id);
		ra.addFlashAttribute("ok", "Requisito eliminado.");
		return "redirect:/analista/" + proyectoId + "/requisitos";
	}

	@GetMapping("/{proyectoId}/casos-uso")
	public String casosUso(@PathVariable String proyectoId, Model model) {
		model.addAttribute("casosUso", casoUsoRepository.findByProyectoId(proyectoId));
		model.addAttribute("requisitos", requisitoRepository.findByProyectoId(proyectoId));
		model.addAttribute("proyecto", proyectoRepository.findById(proyectoId).orElseThrow());
		return "modulos/analista/casos-uso";
	}

	@PostMapping("/{proyectoId}/casos-uso")
	public String guardarCasoUso(@PathVariable String proyectoId, @ModelAttribute CasoUso cu, RedirectAttributes ra) {
		cu.setProyectoId(proyectoId);
		if (cu.getId() == null || cu.getId().isBlank()) {
			cu.setId(null);
		}
		casoUsoRepository.save(cu);
		ra.addFlashAttribute("ok", "Caso de uso guardado.");
		return "redirect:/analista/" + proyectoId + "/casos-uso";
	}

	@GetMapping("/{proyectoId}/diagramas")
	public String diagramas(@PathVariable String proyectoId, Model model) {
		model.addAttribute("diagramas", diagramaRepository.findByProyectoId(proyectoId));
		model.addAttribute("proyecto", proyectoRepository.findById(proyectoId).orElseThrow());
		return "modulos/analista/diagramas";
	}

	@PostMapping("/{proyectoId}/diagramas")
	public String guardarDiagrama(@PathVariable String proyectoId, @ModelAttribute Diagrama d,
			@AuthenticationPrincipal UsuarioPrincipal principal, RedirectAttributes ra) {
		d.setProyectoId(proyectoId);
		d.setAutorId(principal.getId());
		if (d.getId() == null || d.getId().isBlank()) {
			d.setId(null);
		}
		diagramaRepository.save(d);
		ra.addFlashAttribute("ok", "Diagrama guardado.");
		return "redirect:/analista/" + proyectoId + "/diagramas";
	}
}
