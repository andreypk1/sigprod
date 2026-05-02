package com.sigpro.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sigpro.dto.MiembroProyectoDto;
import com.sigpro.dto.ProyectoFormDto;
import com.sigpro.model.Cliente;
import com.sigpro.model.Epica;
import com.sigpro.model.HistoriaUsuario;
import com.sigpro.model.Proyecto;
import com.sigpro.model.Riesgo;
import com.sigpro.model.RolSistema;
import com.sigpro.model.Sprint;
import com.sigpro.model.Usuario;
import com.sigpro.repository.EpicaRepository;
import com.sigpro.repository.HistoriaUsuarioRepository;
import com.sigpro.repository.RiesgoRepository;
import com.sigpro.repository.SprintRepository;
import com.sigpro.repository.TareaRepository;
import com.sigpro.security.UsuarioPrincipal;
import com.sigpro.service.AuditoriaService;
import com.sigpro.service.BusinessRuleException;
import com.sigpro.service.ClienteService;
import com.sigpro.service.ProyectoService;
import com.sigpro.service.UsuarioService;

@Controller
@RequestMapping("/proyectos")
public class ProyectosController {

	private final ProyectoService proyectoService;
	private final ClienteService clienteService;
	private final UsuarioService usuarioService;
	private final AuditoriaService auditoriaService;
	private final EpicaRepository epicaRepository;
	private final HistoriaUsuarioRepository historiaUsuarioRepository;
	private final SprintRepository sprintRepository;
	private final TareaRepository tareaRepository;
	private final RiesgoRepository riesgoRepository;

	public ProyectosController(ProyectoService proyectoService, ClienteService clienteService,
			UsuarioService usuarioService, AuditoriaService auditoriaService, EpicaRepository epicaRepository,
			HistoriaUsuarioRepository historiaUsuarioRepository, SprintRepository sprintRepository,
			TareaRepository tareaRepository, RiesgoRepository riesgoRepository) {
		this.proyectoService = proyectoService;
		this.clienteService = clienteService;
		this.usuarioService = usuarioService;
		this.auditoriaService = auditoriaService;
		this.epicaRepository = epicaRepository;
		this.historiaUsuarioRepository = historiaUsuarioRepository;
		this.sprintRepository = sprintRepository;
		this.tareaRepository = tareaRepository;
		this.riesgoRepository = riesgoRepository;
	}

	@GetMapping
	public String lista(@RequestParam(required = false) String estado,
			@RequestParam(required = false) String metodologia, @RequestParam(required = false) String nombre,
			Model model) {
		List<Proyecto> proyectos = proyectoService.listarFiltrado(estado, metodologia, nombre);
		Map<String, Double> progreso = new HashMap<>();
		for (Proyecto p : proyectos) {
			progreso.put(p.getId(), proyectoService.calcularProgreso(p.getId()));
		}
		model.addAttribute("proyectos", proyectos);
		model.addAttribute("progresoPorProyecto", progreso);
		model.addAttribute("filtroEstado", estado);
		model.addAttribute("filtroMetodologia", metodologia);
		model.addAttribute("filtroNombre", nombre);
		return "proyectos/lista";
	}

	@GetMapping("/nuevo")
	public String nuevoForm(Model model) {
		ProyectoFormDto dto = new ProyectoFormDto();
		dto.getMiembros().add(new MiembroProyectoDto());
		dto.getMiembros().add(new MiembroProyectoDto());
		dto.getMiembros().add(new MiembroProyectoDto());
		model.addAttribute("proyectoForm", dto);
		cargarCatalogos(model);
		return "proyectos/nuevo";
	}

	@PostMapping("/nuevo")
	public String crear(@Valid @ModelAttribute("proyectoForm") ProyectoFormDto dto, BindingResult bindingResult,
			Model model, @AuthenticationPrincipal UsuarioPrincipal principal, RedirectAttributes redirectAttributes) {
		cargarCatalogos(model);
		if (bindingResult.hasErrors()) {
			return "proyectos/nuevo";
		}
		try {
			Proyecto p = proyectoService.crear(dto);
			auditoriaService.registrar(principal.getId(), principal.nombreCompleto(), "CREAR", "PROYECTO", p.getId(),
					null, p.getNombre(), null);
			return "redirect:/proyectos/" + p.getId();
		} catch (BusinessRuleException ex) {
			model.addAttribute("errorGlobal", ex.getMessage());
			return "proyectos/nuevo";
		}
	}

	private void cargarCatalogos(Model model) {
		List<Cliente> clientes = clienteService.listar();
		List<Usuario> pos = usuarioService.listarPorRol(RolSistema.PRODUCT_OWNER);
		List<Usuario> pms = usuarioService.listarPorRol(RolSistema.PROJECT_MANAGER);
		List<Usuario> todos = usuarioService.listarTodos();
		model.addAttribute("clientes", clientes);
		model.addAttribute("productOwners", pos);
		model.addAttribute("projectManagers", pms);
		model.addAttribute("usuariosTodos", todos);
	}

	@GetMapping("/{id}")
	public String detalle(@PathVariable String id, Model model) {
		Proyecto p = proyectoService.findById(id).orElseThrow();
		model.addAttribute("proyecto", p);
		model.addAttribute("progreso", proyectoService.calcularProgreso(id));

		List<Epica> epicas = epicaRepository.findByProyectoId(id);
		model.addAttribute("epicas", epicas);

		Map<String, List<HistoriaUsuario>> historiasPorEpica = new HashMap<>();
		List<HistoriaUsuario> todas = historiaUsuarioRepository.findByProyectoId(id);
		for (Epica e : epicas) {
			historiasPorEpica.put(e.getId(),
					todas.stream().filter(h -> e.getId().equals(h.getEpicaId())).collect(Collectors.toList()));
		}
		model.addAttribute("historiasPorEpica", historiasPorEpica);
		model.addAttribute("historiasSinEpica", todas.stream().filter(h -> h.getEpicaId() == null).toList());

		List<Sprint> sprints = sprintRepository.findByProyectoId(id);
		model.addAttribute("sprints", sprints);

		List<Riesgo> riesgos = riesgoRepository.findByProyectoId(id);
		model.addAttribute("riesgos", riesgos);

		double horasReales = tareaRepository.findByProyectoId(id).stream().map(t -> t.getHorasReales())
				.filter(h -> h != null).mapToDouble(Double::doubleValue).sum();
		model.addAttribute("horasRealesTotal", horasReales);
		model.addAttribute("presupuesto", p.getPresupuesto() != null ? p.getPresupuesto() : 0);

		return "proyectos/detalle";
	}

	/**
	 * Atajo al backlog web del proyecto (épicas + CRUD de historias de usuario).
	 * Misma política que {@code GET /proyectos/{id}} vía {@code /proyectos/**}.
	 */
	@GetMapping("/{id}/historias")
	public String historiasUsuario(@PathVariable String id) {
		proyectoService.findById(id).orElseThrow();
		return "redirect:/backlog?proyectoId=" + id;
	}
}
