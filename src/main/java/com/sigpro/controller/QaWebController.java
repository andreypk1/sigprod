package com.sigpro.controller;

import java.time.LocalDateTime;

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

import com.sigpro.model.CasoPrueba;
import com.sigpro.model.Defecto;
import com.sigpro.model.PlanPruebas;
import com.sigpro.repository.CasoPruebaRepository;
import com.sigpro.repository.DefectoRepository;
import com.sigpro.repository.PlanPruebasRepository;
import com.sigpro.repository.ProyectoRepository;
import com.sigpro.repository.RequisitoRepository;
import com.sigpro.security.UsuarioPrincipal;

@Controller
@RequestMapping("/qa")
@PreAuthorize("hasAnyRole('ADMINISTRADOR','QA_ENGINEER','PROJECT_MANAGER')")
public class QaWebController {

	private final PlanPruebasRepository planPruebasRepository;
	private final CasoPruebaRepository casoPruebaRepository;
	private final DefectoRepository defectoRepository;
	private final ProyectoRepository proyectoRepository;
	private final RequisitoRepository requisitoRepository;

	public QaWebController(PlanPruebasRepository planPruebasRepository, CasoPruebaRepository casoPruebaRepository,
			DefectoRepository defectoRepository, ProyectoRepository proyectoRepository,
			RequisitoRepository requisitoRepository) {
		this.planPruebasRepository = planPruebasRepository;
		this.casoPruebaRepository = casoPruebaRepository;
		this.defectoRepository = defectoRepository;
		this.proyectoRepository = proyectoRepository;
		this.requisitoRepository = requisitoRepository;
	}

	@GetMapping({"", "/"})
	public String index(Model model) {
		model.addAttribute("proyectos", proyectoRepository.findAll());
		return "modulos/qa/index";
	}

	@GetMapping("/{proyectoId}/planes")
	public String planes(@PathVariable String proyectoId, Model model) {
		model.addAttribute("planes", planPruebasRepository.findByProyectoId(proyectoId));
		model.addAttribute("proyecto", proyectoRepository.findById(proyectoId).orElseThrow());
		return "modulos/qa/planes";
	}

	@PostMapping("/{proyectoId}/planes")
	public String guardarPlan(@PathVariable String proyectoId, @ModelAttribute PlanPruebas plan,
			@AuthenticationPrincipal UsuarioPrincipal principal, RedirectAttributes ra) {
		plan.setProyectoId(proyectoId);
		plan.setQaId(principal.getId());
		if (plan.getId() == null || plan.getId().isBlank()) {
			plan.setId(null);
		}
		planPruebasRepository.save(plan);
		ra.addFlashAttribute("ok", "Plan de pruebas guardado.");
		return "redirect:/qa/" + proyectoId + "/planes";
	}

	@GetMapping("/{proyectoId}/planes/{planId}/casos")
	public String casos(@PathVariable String proyectoId, @PathVariable String planId, Model model) {
		model.addAttribute("casos", casoPruebaRepository.findByPlanId(planId));
		model.addAttribute("plan", planPruebasRepository.findById(planId).orElseThrow());
		model.addAttribute("proyecto", proyectoRepository.findById(proyectoId).orElseThrow());
		model.addAttribute("requisitos", requisitoRepository.findByProyectoId(proyectoId));
		return "modulos/qa/casos";
	}

	@PostMapping("/{proyectoId}/planes/{planId}/casos")
	public String guardarCaso(@PathVariable String proyectoId, @PathVariable String planId,
			@ModelAttribute CasoPrueba caso, RedirectAttributes ra) {
		caso.setPlanId(planId);
		caso.setProyectoId(proyectoId);
		if (caso.getId() == null || caso.getId().isBlank()) {
			caso.setId(null);
		}
		casoPruebaRepository.save(caso);
		ra.addFlashAttribute("ok", "Caso de prueba guardado.");
		return "redirect:/qa/" + proyectoId + "/planes/" + planId + "/casos";
	}

	@GetMapping("/{proyectoId}/defectos")
	public String defectos(@PathVariable String proyectoId, Model model) {
		model.addAttribute("defectos", defectoRepository.findByProyectoId(proyectoId));
		model.addAttribute("proyecto", proyectoRepository.findById(proyectoId).orElseThrow());
		return "modulos/qa/defectos";
	}

	@PostMapping("/{proyectoId}/defectos")
	public String guardarDefecto(@PathVariable String proyectoId, @ModelAttribute Defecto defecto,
			@AuthenticationPrincipal UsuarioPrincipal principal, RedirectAttributes ra) {
		defecto.setProyectoId(proyectoId);
		defecto.setReportadoPorId(principal.getId());
		defecto.setFechaReporte(LocalDateTime.now());
		if (defecto.getId() == null || defecto.getId().isBlank()) {
			defecto.setId(null);
		}
		defectoRepository.save(defecto);
		ra.addFlashAttribute("ok", "Defecto registrado.");
		return "redirect:/qa/" + proyectoId + "/defectos";
	}
}
