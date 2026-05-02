package com.sigpro.controller;

import java.time.LocalDate;

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

import com.sigpro.model.Adr;
import com.sigpro.model.Diagrama;
import com.sigpro.repository.AdrRepository;
import com.sigpro.repository.DiagramaRepository;
import com.sigpro.repository.ProyectoRepository;
import com.sigpro.security.UsuarioPrincipal;

@Controller
@RequestMapping("/arquitectura")
@PreAuthorize("hasAnyRole('ADMINISTRADOR','ARQUITECTO_SOFTWARE')")
public class ArquitectoWebController {

	private final AdrRepository adrRepository;
	private final DiagramaRepository diagramaRepository;
	private final ProyectoRepository proyectoRepository;

	public ArquitectoWebController(AdrRepository adrRepository, DiagramaRepository diagramaRepository,
			ProyectoRepository proyectoRepository) {
		this.adrRepository = adrRepository;
		this.diagramaRepository = diagramaRepository;
		this.proyectoRepository = proyectoRepository;
	}

	@GetMapping({"", "/"})
	public String index(Model model) {
		model.addAttribute("proyectos", proyectoRepository.findAll());
		return "modulos/arquitecto/index";
	}

	@GetMapping("/{proyectoId}/adrs")
	public String adrs(@PathVariable String proyectoId, Model model) {
		model.addAttribute("adrs", adrRepository.findByProyectoIdOrderByNumeroAsc(proyectoId));
		model.addAttribute("proyecto", proyectoRepository.findById(proyectoId).orElseThrow());
		return "modulos/arquitecto/adrs";
	}

	@PostMapping("/{proyectoId}/adrs")
	public String guardarAdr(@PathVariable String proyectoId, @ModelAttribute Adr adr,
			@AuthenticationPrincipal UsuarioPrincipal principal, RedirectAttributes ra) {
		adr.setProyectoId(proyectoId);
		adr.setArquitectoId(principal.getId());
		adr.setFecha(LocalDate.now());
		boolean nuevo = adr.getId() == null || adr.getId().isBlank();
		if (nuevo) {
			adr.setId(null);
			if (adr.getNumero() == null) {
				long count = adrRepository.findByProyectoIdOrderByNumeroAsc(proyectoId).size();
				adr.setNumero((int) count + 1);
			}
		}
		adrRepository.save(adr);
		ra.addFlashAttribute("ok", "ADR guardado.");
		return "redirect:/arquitectura/" + proyectoId + "/adrs";
	}

	@GetMapping("/{proyectoId}/adrs/{id}/eliminar")
	public String eliminarAdr(@PathVariable String proyectoId, @PathVariable String id, RedirectAttributes ra) {
		adrRepository.deleteById(id);
		ra.addFlashAttribute("ok", "ADR eliminado.");
		return "redirect:/arquitectura/" + proyectoId + "/adrs";
	}

	@GetMapping("/{proyectoId}/diagramas")
	public String diagramas(@PathVariable String proyectoId, Model model) {
		model.addAttribute("diagramas", diagramaRepository.findByProyectoId(proyectoId));
		model.addAttribute("proyecto", proyectoRepository.findById(proyectoId).orElseThrow());
		return "modulos/arquitecto/diagramas";
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
		return "redirect:/arquitectura/" + proyectoId + "/diagramas";
	}
}
