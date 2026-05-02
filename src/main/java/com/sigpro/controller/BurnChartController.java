package com.sigpro.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sigpro.model.Proyecto;
import com.sigpro.model.Sprint;
import com.sigpro.repository.SprintRepository;
import com.sigpro.service.BurnChartService;
import com.sigpro.service.BurnChartService.BurnupChartTriple;
import com.sigpro.service.BurnChartService.SprintBurndown;
import com.sigpro.service.ProyectoService;

@Controller
@RequestMapping("/proyectos")
public class BurnChartController {

	private final ProyectoService proyectoService;
	private final BurnChartService burnChartService;
	private final SprintRepository sprintRepository;

	public BurnChartController(ProyectoService proyectoService, BurnChartService burnChartService,
			SprintRepository sprintRepository) {
		this.proyectoService = proyectoService;
		this.burnChartService = burnChartService;
		this.sprintRepository = sprintRepository;
	}

	@GetMapping("/{id}/burncharts")
	public String metricasBurnCharts(@PathVariable String id, @RequestParam(required = false) String sprintId,
			Model model) {
		Proyecto proyecto = proyectoService.findById(id).orElseThrow();
		model.addAttribute("proyecto", proyecto);

		List<Sprint> sprints = sprintRepository.findByProyectoId(id).stream()
				.sorted(Comparator.comparing(Sprint::getNumero, Comparator.nullsLast(Integer::compareTo)))
				.toList();
		model.addAttribute("sprints", sprints);

		Sprint seleccionado = BurnChartService.buscarSprint(sprints, sprintId);
		if (seleccionado == null) {
			seleccionado = BurnChartService.elegirSprintDefecto(sprints);
		}
		model.addAttribute("sprintSeleccionado", seleccionado);
		model.addAttribute("sprintIdParam", seleccionado != null ? seleccionado.getId() : null);

		SprintBurndown burndownSprint = seleccionado != null
				? burnChartService.buildSprintBurndown(seleccionado, proyecto)
				: SprintBurndown.vacio();
		if (burndownSprint.getLabels().isEmpty()) {
			model.addAttribute("etiquetasBurndown", List.of("—"));
			model.addAttribute("burndownData", List.of(0d));
			model.addAttribute("idealBurndown", List.of(0d));
		} else {
			model.addAttribute("etiquetasBurndown", burndownSprint.getLabels());
			model.addAttribute("burndownData", burndownSprint.getRemainingReal());
			model.addAttribute("idealBurndown", burndownSprint.getIdealBurndown());
		}
		model.addAttribute("puntosSprintComprometidos", burndownSprint.getCommittedStoryPoints());

		BurnupChartTriple burnup = burnChartService.alignBurnupChartSeries(proyecto);
		model.addAttribute("burnupData", burnup.real());
		model.addAttribute("idealBurnup", burnup.ideal());
		model.addAttribute("etiquetas", burnup.labels());

		int totalPuntos = proyecto.getTotalPuntos();
		model.addAttribute("totalPuntos", totalPuntos);
		model.addAttribute("puntosCompletados", burnChartService.puntosCompletadosUltimo(proyecto));
		double velocidad = burnChartService.calcularVelocidadPromedio(proyecto);
		model.addAttribute("velocidadPromedio", velocidad);
		int pendientes = burnChartService.puntosPendientes(proyecto);
		model.addAttribute("sprintsRestantes", burnChartService.calcularSprintsRestantes(pendientes, velocidad));

		boolean proyectoTieneSeriesBurnup = (proyecto.getBurnupAcumulado() != null
				&& !proyecto.getBurnupAcumulado().isEmpty())
				|| (proyecto.getEtiquetasBurnSprints() != null && !proyecto.getEtiquetasBurnSprints().isEmpty());
		model.addAttribute("proyectoTieneSeriesBurnup", proyectoTieneSeriesBurnup);

		return "proyectos/burncharts";
	}
}
