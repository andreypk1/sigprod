package com.sigpro.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sigpro.model.Epica;
import com.sigpro.model.HistoriaUsuario;
import com.sigpro.model.Proyecto;
import com.sigpro.repository.EpicaRepository;
import com.sigpro.repository.HistoriaUsuarioRepository;
import com.sigpro.repository.ProyectoRepository;
import com.sigpro.repository.SprintRepository;
import com.sigpro.security.UsuarioPrincipal;
import com.sigpro.service.AuditoriaService;
import com.sigpro.service.EpicaService;
import com.sigpro.service.HistoriaUsuarioService;
import com.sigpro.service.ProyectoService;

@Controller
@RequestMapping("/backlog")
public class BacklogWebController {

	private final ProyectoService proyectoService;
	private final ProyectoRepository proyectoRepository;
	private final EpicaRepository epicaRepository;
	private final EpicaService epicaService;
	private final HistoriaUsuarioRepository historiaUsuarioRepository;
	private final HistoriaUsuarioService historiaUsuarioService;
	private final SprintRepository sprintRepository;
	private final AuditoriaService auditoriaService;

	public BacklogWebController(ProyectoService proyectoService, ProyectoRepository proyectoRepository,
			EpicaRepository epicaRepository, EpicaService epicaService,
			HistoriaUsuarioRepository historiaUsuarioRepository, HistoriaUsuarioService historiaUsuarioService,
			SprintRepository sprintRepository, AuditoriaService auditoriaService) {
		this.proyectoService = proyectoService;
		this.proyectoRepository = proyectoRepository;
		this.epicaRepository = epicaRepository;
		this.epicaService = epicaService;
		this.historiaUsuarioRepository = historiaUsuarioRepository;
		this.historiaUsuarioService = historiaUsuarioService;
		this.sprintRepository = sprintRepository;
		this.auditoriaService = auditoriaService;
	}

	@GetMapping({"", "/"})
	public String index(@RequestParam(required = false) String proyectoId,
			@RequestParam(required = false) String editEpica,
			@RequestParam(required = false) String editHistoria,
			Model model) {
		List<Proyecto> proyectos = proyectoService.listarFiltrado(null, null, null);
		model.addAttribute("proyectos", proyectos);
		model.addAttribute("proyectoId", proyectoId);

		Epica epicaForm = new Epica();
		HistoriaUsuario historiaForm = new HistoriaUsuario();

		if (proyectoId != null && !proyectoId.isBlank()) {
			proyectoService.findById(proyectoId).ifPresent(p -> model.addAttribute("proyecto", p));
			model.addAttribute("epicas", epicaRepository.findByProyectoId(proyectoId));
			model.addAttribute("historias", historiaUsuarioRepository.findByProyectoId(proyectoId));
			model.addAttribute("sprintsSelect", sprintRepository.findByProyectoId(proyectoId));

			if (editEpica != null && !editEpica.isBlank()) {
				epicaForm = epicaService.findById(editEpica).orElse(epicaForm);
			}
			epicaForm.setProyectoId(proyectoId);

			if (editHistoria != null && !editHistoria.isBlank()) {
				historiaForm = historiaUsuarioService.findById(editHistoria).orElse(historiaForm);
			}
			if (historiaForm.getProyectoId() == null || historiaForm.getProyectoId().isBlank()) {
				historiaForm.setProyectoId(proyectoId);
			}
		}

		model.addAttribute("epica", epicaForm);
		model.addAttribute("historia", historiaForm);
		return "modulos/backlog";
	}

	@PostMapping("/epica/guardar")
	public String guardarEpica(Epica epica, RedirectAttributes ra) {
		epicaService.guardar(epica);
		ra.addFlashAttribute("msg", "Épica guardada.");
		return redirectBack(epica.getProyectoId());
	}

	@PostMapping("/epica/{id}/eliminar")
	public String eliminarEpica(@PathVariable String id, @RequestParam String proyectoId, RedirectAttributes ra) {
		epicaService.eliminar(id);
		ra.addFlashAttribute("msg", "Épica eliminada.");
		return redirectBack(proyectoId);
	}

	@PostMapping("/historia/guardar")
	public String guardarHistoria(HistoriaUsuario historia, RedirectAttributes ra) {
		historiaUsuarioService.guardar(historia);
		ra.addFlashAttribute("msg", "Historia de usuario guardada.");
		return redirectBack(historia.getProyectoId());
	}

	@PostMapping("/historia/{id}/eliminar")
	public String eliminarHistoria(@PathVariable String id, @RequestParam String proyectoId, RedirectAttributes ra) {
		historiaUsuarioService.eliminar(id);
		ra.addFlashAttribute("msg", "Historia eliminada.");
		return redirectBack(proyectoId);
	}

	private static String redirectBack(String proyectoId) {
		if (proyectoId == null || proyectoId.isBlank()) {
			return "redirect:/backlog";
		}
		return "redirect:/backlog?proyectoId=" + proyectoId;
	}

	@GetMapping("/aceptacion")
	@PreAuthorize("hasAnyRole('PRODUCT_OWNER','ADMINISTRADOR')")
	public String tableroAceptacion(Model model, @AuthenticationPrincipal UsuarioPrincipal principal) {
		List<Proyecto> misProy = proyectoRepository.findByProductOwnerId(principal.getId());
		List<String> ids = misProy.stream().map(Proyecto::getId).toList();
		List<HistoriaUsuario> paraAceptar = ids.isEmpty() ? List.of()
				: historiaUsuarioRepository.findByProyectoIdInAndEstadoIn(ids,
						List.of("EN_REVISION", "PARA_ACEPTACION"));
		model.addAttribute("historias", paraAceptar);
		model.addAttribute("proyectos", misProy);
		return "modulos/po/tablero-aceptacion";
	}

	@PostMapping("/aceptacion/{id}/aprobar")
	@PreAuthorize("hasAnyRole('PRODUCT_OWNER','ADMINISTRADOR')")
	public String aprobar(@PathVariable String id, @RequestParam(required = false) String observacion,
			@AuthenticationPrincipal UsuarioPrincipal principal, RedirectAttributes ra) {
		historiaUsuarioRepository.findById(id).ifPresent(h -> {
			h.setEstado("ACEPTADA");
			historiaUsuarioRepository.save(h);
			auditoriaService.registrar(principal.getId(), principal.nombreCompleto(), "APROBAR_HU", "HISTORIA_USUARIO",
					id, null, observacion, "internal");
		});
		ra.addFlashAttribute("ok", "Historia aceptada.");
		return "redirect:/backlog/aceptacion";
	}

	@PostMapping("/aceptacion/{id}/rechazar")
	@PreAuthorize("hasAnyRole('PRODUCT_OWNER','ADMINISTRADOR')")
	public String rechazar(@PathVariable String id, @RequestParam(required = false) String observacion,
			@AuthenticationPrincipal UsuarioPrincipal principal, RedirectAttributes ra) {
		historiaUsuarioRepository.findById(id).ifPresent(h -> {
			h.setEstado("RECHAZADA");
			historiaUsuarioRepository.save(h);
			auditoriaService.registrar(principal.getId(), principal.nombreCompleto(), "RECHAZAR_HU", "HISTORIA_USUARIO",
					id, null, observacion, "internal");
		});
		ra.addFlashAttribute("ok", "Historia rechazada.");
		return "redirect:/backlog/aceptacion";
	}
}
