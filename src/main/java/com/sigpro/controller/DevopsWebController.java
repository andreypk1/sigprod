package com.sigpro.controller;

import java.time.LocalDateTime;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sigpro.model.Ambiente;
import com.sigpro.model.Incidente;
import com.sigpro.model.PipelineCicd;
import com.sigpro.repository.AmbienteRepository;
import com.sigpro.repository.IncidenteRepository;
import com.sigpro.repository.PipelineCicdRepository;
import com.sigpro.repository.ProyectoRepository;

@Controller
@RequestMapping("/devops")
@PreAuthorize("hasAnyRole('ADMINISTRADOR','DEVOPS_ENGINEER')")
public class DevopsWebController {

	private final PipelineCicdRepository pipelineRepository;
	private final AmbienteRepository ambienteRepository;
	private final IncidenteRepository incidenteRepository;
	private final ProyectoRepository proyectoRepository;

	public DevopsWebController(PipelineCicdRepository pipelineRepository, AmbienteRepository ambienteRepository,
			IncidenteRepository incidenteRepository, ProyectoRepository proyectoRepository) {
		this.pipelineRepository = pipelineRepository;
		this.ambienteRepository = ambienteRepository;
		this.incidenteRepository = incidenteRepository;
		this.proyectoRepository = proyectoRepository;
	}

	@GetMapping({"", "/"})
	public String index(Model model) {
		model.addAttribute("proyectos", proyectoRepository.findAll());
		return "modulos/devops/index";
	}

	@GetMapping("/{proyectoId}/pipelines")
	public String pipelines(@PathVariable String proyectoId, Model model) {
		model.addAttribute("pipelines", pipelineRepository.findByProyectoId(proyectoId));
		model.addAttribute("proyecto", proyectoRepository.findById(proyectoId).orElseThrow());
		return "modulos/devops/pipelines";
	}

	@PostMapping("/{proyectoId}/pipelines")
	public String guardarPipeline(@PathVariable String proyectoId, @ModelAttribute PipelineCicd pipeline,
			RedirectAttributes ra) {
		pipeline.setProyectoId(proyectoId);
		if (pipeline.getId() == null || pipeline.getId().isBlank()) {
			pipeline.setId(null);
		}
		pipelineRepository.save(pipeline);
		ra.addFlashAttribute("ok", "Pipeline guardado.");
		return "redirect:/devops/" + proyectoId + "/pipelines";
	}

	@GetMapping("/{proyectoId}/ambientes")
	public String ambientes(@PathVariable String proyectoId, Model model) {
		model.addAttribute("ambientes", ambienteRepository.findByProyectoId(proyectoId));
		model.addAttribute("proyecto", proyectoRepository.findById(proyectoId).orElseThrow());
		return "modulos/devops/ambientes";
	}

	@PostMapping("/{proyectoId}/ambientes")
	public String guardarAmbiente(@PathVariable String proyectoId, @ModelAttribute Ambiente ambiente,
			RedirectAttributes ra) {
		ambiente.setProyectoId(proyectoId);
		if (ambiente.getId() == null || ambiente.getId().isBlank()) {
			ambiente.setId(null);
		}
		ambienteRepository.save(ambiente);
		ra.addFlashAttribute("ok", "Ambiente guardado.");
		return "redirect:/devops/" + proyectoId + "/ambientes";
	}

	@GetMapping("/{proyectoId}/incidentes")
	public String incidentes(@PathVariable String proyectoId, Model model) {
		model.addAttribute("incidentes", incidenteRepository.findByProyectoId(proyectoId));
		model.addAttribute("proyecto", proyectoRepository.findById(proyectoId).orElseThrow());
		return "modulos/devops/incidentes";
	}

	@PostMapping("/{proyectoId}/incidentes")
	public String guardarIncidente(@PathVariable String proyectoId, @ModelAttribute Incidente incidente,
			RedirectAttributes ra) {
		incidente.setProyectoId(proyectoId);
		incidente.setFechaReporte(LocalDateTime.now());
		if (incidente.getId() == null || incidente.getId().isBlank()) {
			incidente.setId(null);
		}
		incidenteRepository.save(incidente);
		ra.addFlashAttribute("ok", "Incidente registrado.");
		return "redirect:/devops/" + proyectoId + "/incidentes";
	}
}
