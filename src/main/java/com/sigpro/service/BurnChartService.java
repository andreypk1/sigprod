package com.sigpro.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sigpro.model.HistoriaUsuario;
import com.sigpro.model.Proyecto;
import com.sigpro.model.Sprint;
import com.sigpro.model.Tarea;
import com.sigpro.repository.HistoriaUsuarioRepository;
import com.sigpro.repository.TareaRepository;
import com.sigpro.util.EstadosTarea;

@Service
public class BurnChartService {

	private static final DateTimeFormatter ETIQUETA_DIA = DateTimeFormatter.ofPattern("dd/MM");

	private final HistoriaUsuarioRepository historiaUsuarioRepository;
	private final TareaRepository tareaRepository;

	public BurnChartService(HistoriaUsuarioRepository historiaUsuarioRepository, TareaRepository tareaRepository) {
		this.historiaUsuarioRepository = historiaUsuarioRepository;
		this.tareaRepository = tareaRepository;
	}

	public List<Double> buildBurnupData(Proyecto proyecto) {
		if (proyecto.getBurnupAcumulado() != null && !proyecto.getBurnupAcumulado().isEmpty()) {
			return new ArrayList<>(proyecto.getBurnupAcumulado());
		}
		return Collections.singletonList(0d);
	}

	/**
	 * Burndown por sprint (solo tareas). Para alinear con el burnup del proyecto use
	 * {@link #buildSprintBurndown(Sprint, Proyecto)}.
	 */
	public SprintBurndown buildSprintBurndown(Sprint sprint) {
		return buildSprintBurndown(sprint, null);
	}

	/**
	 * Burndown por sprint: si el proyecto tiene {@link Proyecto#getBurnupAcumulado()} alineado con el número de sprint,
	 * la serie <strong>real</strong> refleja los puntos entregados en ese sprint (delta del burnup), coherente con la
	 * gráfica de burnup. Si no hay serie o no aplica el índice, se usa la estimación por tareas completadas en el sprint.
	 */
	public SprintBurndown buildSprintBurndown(Sprint sprint, Proyecto proyecto) {
		if (sprint == null || sprint.getId() == null) {
			return SprintBurndown.vacio();
		}
		SprintBurndown desdeTareas = buildSprintBurndownDesdeTareas(sprint);
		Double deltaBurnup = proyecto != null ? burnupDeltaParaSprint(proyecto, sprint.getNumero()) : null;
		if (deltaBurnup == null) {
			return desdeTareas;
		}
		int ptsHu = desdeTareas.getCommittedStoryPoints();
		return mergeBurndownConBurnup(sprint, desdeTareas, ptsHu, deltaBurnup);
	}

	/** Story points entregados en el sprint N según el burnup acumulado del proyecto (índice N = tras sprint N). */
	public static Double burnupDeltaParaSprint(Proyecto proyecto, Integer numeroSprint) {
		if (proyecto == null || numeroSprint == null || numeroSprint < 1) {
			return null;
		}
		List<Double> b = proyecto.getBurnupAcumulado();
		if (b == null || b.isEmpty()) {
			return null;
		}
		int idx = numeroSprint;
		if (idx >= b.size()) {
			return null;
		}
		double prev = b.get(idx - 1);
		double cur = b.get(idx);
		double delta = cur - prev;
		return delta;
	}

	private SprintBurndown mergeBurndownConBurnup(Sprint sprint, SprintBurndown desdeTareas, int puntosHu,
			double deltaBurnup) {
		List<String> labels = desdeTareas.getLabels();
		int n = labels.size();
		if (n == 0) {
			return desdeTareas;
		}
		double alcance = Math.max(puntosHu, deltaBurnup);
		double restanteFinal = Math.max(0.0, alcance - deltaBurnup);
		int denom = Math.max(1, n - 1);

		List<Double> ideal = new ArrayList<>(n);
		List<Double> real = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			double t = (double) (n - 1 - i) / denom;
			ideal.add(alcance * t);
			real.add(restanteFinal + (alcance - restanteFinal) * t);
		}

		int committedEntero = (int) Math.round(alcance);
		return new SprintBurndown(labels, ideal, real, committedEntero, sprint);
	}

	/**
	 * Burndown clásico por un sprint concreto: el alcance es la suma de puntos de las HU comprometidas en ese sprint,
	 * y el eje X son los días naturales entre inicio y fin del sprint (no todos los sprints del proyecto).
	 */
	private SprintBurndown buildSprintBurndownDesdeTareas(Sprint sprint) {
		LocalDate start = sprint.getFechaInicio() != null ? sprint.getFechaInicio() : LocalDate.now();
		LocalDate end = sprint.getFechaFin() != null ? sprint.getFechaFin() : start.plusWeeks(2);
		if (end.isBefore(start)) {
			end = start.plusWeeks(2);
		}
		if (!end.isAfter(start)) {
			end = start.plusDays(1);
		}

		List<HistoriaUsuario> historias = historiaUsuarioRepository.findBySprintId(sprint.getId());
		int committed = historias.stream().mapToInt(BurnChartService::puntosHistoria).sum();

		List<Tarea> tareasSprint = tareaRepository.findBySprintId(sprint.getId());

		List<LocalDate> dias = new ArrayList<>();
		for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
			dias.add(d);
		}
		if (dias.isEmpty()) {
			dias.add(start);
		}

		int n = dias.size();
		List<Double> ideal = new ArrayList<>(n);
		List<Double> real = new ArrayList<>(n);
		int denom = Math.max(1, n - 1);
		for (int i = 0; i < n; i++) {
			ideal.add(committed * (double) (n - 1 - i) / denom);
		}

		for (LocalDate dia : dias) {
			int completadas = 0;
			for (HistoriaUsuario h : historias) {
				if (historiaTerminadaAl(h, tareasSprint, dia)) {
					completadas += puntosHistoria(h);
				}
			}
			real.add((double) Math.max(0, committed - completadas));
		}

		List<String> etiquetas = dias.stream().map(d -> d.format(ETIQUETA_DIA)).collect(Collectors.toList());
		return new SprintBurndown(etiquetas, ideal, real, committed, sprint);
	}

	private static int puntosHistoria(HistoriaUsuario h) {
		return h.getEstimacionPuntos() != null ? h.getEstimacionPuntos() : 0;
	}

	/**
	 * La historia cuenta como entregada a partir del día en que todas sus tareas del sprint quedan COMPLETADA
	 * y la fecha de actualización (o creación) no supera el día considerado.
	 */
	private boolean historiaTerminadaAl(HistoriaUsuario h, List<Tarea> tareasSprint, LocalDate dia) {
		List<Tarea> ts = tareasSprint.stream()
				.filter(t -> Objects.equals(h.getId(), t.getHistoriaId()))
				.toList();
		if (ts.isEmpty()) {
			return false;
		}
		return ts.stream().allMatch(t -> tareaCompletadaAl(t, dia));
	}

	private boolean tareaCompletadaAl(Tarea t, LocalDate dia) {
		if (!EstadosTarea.COMPLETADA.equals(t.getEstado())) {
			return false;
		}
		if (t.getFechaActualizacion() != null) {
			return !t.getFechaActualizacion().toLocalDate().isAfter(dia);
		}
		if (t.getFechaCreacion() != null) {
			return !t.getFechaCreacion().toLocalDate().isAfter(dia);
		}
		return true;
	}

	public static final class SprintBurndown {
		private final List<String> labels;
		private final List<Double> idealBurndown;
		private final List<Double> remainingReal;
		private final int committedStoryPoints;
		private final Sprint sprint;

		private SprintBurndown(List<String> labels, List<Double> idealBurndown, List<Double> remainingReal,
				int committedStoryPoints, Sprint sprint) {
			this.labels = labels;
			this.idealBurndown = idealBurndown;
			this.remainingReal = remainingReal;
			this.committedStoryPoints = committedStoryPoints;
			this.sprint = sprint;
		}

		public static SprintBurndown vacio() {
			return new SprintBurndown(List.of(), List.of(), List.of(), 0, null);
		}

		public List<String> getLabels() {
			return labels;
		}

		public List<Double> getIdealBurndown() {
			return idealBurndown;
		}

		public List<Double> getRemainingReal() {
			return remainingReal;
		}

		public int getCommittedStoryPoints() {
			return committedStoryPoints;
		}

		public Sprint getSprint() {
			return sprint;
		}
	}

	/** Línea ideal de burnup (producto) en el tiempo de sprints — sin cambios de semántica. */
	public List<Double> buildIdealLine(Proyecto proyecto) {
		int total = proyecto.getTotalPuntos();
		int n = countSegments(proyecto);
		return buildIdealBurnup(total, n);
	}

	public List<String> buildLabels(Proyecto proyecto) {
		if (proyecto.getEtiquetasBurnSprints() != null && !proyecto.getEtiquetasBurnSprints().isEmpty()) {
			return new ArrayList<>(proyecto.getEtiquetasBurnSprints());
		}
		List<Double> burnup = proyecto.getBurnupAcumulado();
		if (burnup == null || burnup.isEmpty()) {
			return Collections.singletonList("Inicio");
		}
		int m = burnup.size();
		List<String> out = new ArrayList<>(m);
		for (int i = 0; i < m; i++) {
			out.add(i == 0 ? "Inicio" : "Sprint " + i);
		}
		return out;
	}

	/**
	 * Recorta etiquetas, burnup real e ideal al mismo tamaño (evita fallos de Chart.js con listas desalineadas en BD).
	 */
	public BurnupChartTriple alignBurnupChartSeries(Proyecto proyecto) {
		List<String> labels = buildLabels(proyecto);
		List<Double> real = new ArrayList<>(buildBurnupData(proyecto));
		List<Double> ideal = new ArrayList<>(buildIdealLine(proyecto));
		int n = Math.min(labels.size(), Math.min(real.size(), ideal.size()));
		if (n <= 0) {
			return new BurnupChartTriple(List.of("Inicio"), List.of(0d), List.of(0d));
		}
		return new BurnupChartTriple(new ArrayList<>(labels.subList(0, n)), new ArrayList<>(real.subList(0, n)),
				new ArrayList<>(ideal.subList(0, n)));
	}

	public record BurnupChartTriple(List<String> labels, List<Double> real, List<Double> ideal) {
	}

	private int countSegments(Proyecto proyecto) {
		List<String> labels = proyecto.getEtiquetasBurnSprints();
		if (labels != null && !labels.isEmpty()) {
			return Math.max(1, labels.size() - 1);
		}
		List<Double> burnup = proyecto.getBurnupAcumulado();
		if (burnup != null && !burnup.isEmpty()) {
			return Math.max(1, burnup.size() - 1);
		}
		return 1;
	}

	public List<Double> buildIdealBurnup(int totalPuntos, int totalSprints) {
		List<Double> ideal = new ArrayList<>();
		if (totalSprints <= 0) {
			ideal.add(0d);
			return ideal;
		}
		for (int i = 0; i <= totalSprints; i++) {
			ideal.add((double) totalPuntos * i / totalSprints);
		}
		return ideal;
	}

	public double calcularVelocidadPromedio(Proyecto proyecto) {
		List<Double> burnup = buildBurnupData(proyecto);
		if (burnup.size() < 2) {
			return 0;
		}
		double sumDelta = 0;
		int count = 0;
		for (int i = 1; i < burnup.size(); i++) {
			double d = burnup.get(i) - burnup.get(i - 1);
			if (d > 0) {
				sumDelta += d;
				count++;
			}
		}
		if (count == 0) {
			return 0;
		}
		return Math.round((sumDelta / count) * 10.0) / 10.0;
	}

	public int calcularSprintsRestantes(int puntosPendientes, double velocidad) {
		if (velocidad <= 0 || puntosPendientes <= 0) {
			return 0;
		}
		return (int) Math.ceil(puntosPendientes / velocidad);
	}

	public int puntosCompletadosUltimo(Proyecto proyecto) {
		List<Double> b = buildBurnupData(proyecto);
		if (b.isEmpty()) {
			return 0;
		}
		return (int) Math.round(b.get(b.size() - 1));
	}

	public int puntosPendientes(Proyecto proyecto) {
		int total = proyecto.getTotalPuntos();
		return Math.max(0, total - puntosCompletadosUltimo(proyecto));
	}

	/**
	 * Elige el sprint por defecto para el burndown: el activo, o el de mayor número, o el primero.
	 */
	public static Sprint elegirSprintDefecto(List<Sprint> sprints) {
		if (sprints == null || sprints.isEmpty()) {
			return null;
		}
		return sprints.stream()
				.filter(s -> "ACTIVO".equals(s.getEstado()))
				.findFirst()
				.orElseGet(() -> sprints.stream()
						.max(Comparator.comparing(Sprint::getNumero, Comparator.nullsFirst(Integer::compareTo)))
						.orElse(sprints.get(0)));
	}

	public static Sprint buscarSprint(List<Sprint> sprints, String sprintId) {
		if (sprintId == null || sprintId.isBlank() || sprints == null) {
			return null;
		}
		return sprints.stream().filter(s -> Objects.equals(sprintId, s.getId())).findFirst().orElse(null);
	}
}
