package com.sigpro.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sigpro.model.HistoriaUsuario;
import com.sigpro.model.Proyecto;
import com.sigpro.model.Sprint;
import com.sigpro.model.Tarea;
import com.sigpro.repository.HistoriaUsuarioRepository;
import com.sigpro.repository.TareaRepository;
import com.sigpro.util.EstadosTarea;

@ExtendWith(MockitoExtension.class)
class BurnChartServiceTest {

	@Mock
	private HistoriaUsuarioRepository historiaUsuarioRepository;

	@Mock
	private TareaRepository tareaRepository;

	@InjectMocks
	private BurnChartService burnChartService;

	@Test
	void buildSprintBurndown_sprintNull_returnsVacio() {
		assertThat(burnChartService.buildSprintBurndown(null).getCommittedStoryPoints()).isZero();
		assertThat(burnChartService.buildSprintBurndown(null).getLabels()).isEmpty();
	}

	@Test
	void buildSprintBurndown_tasksCompletedMidSprint_burnsRemainingAfterCompletionDay() {
		String sprintId = "s1";
		LocalDate ini = LocalDate.of(2026, 5, 1);
		LocalDate fin = LocalDate.of(2026, 5, 3);

		Sprint s = new Sprint();
		s.setId(sprintId);
		s.setFechaInicio(ini);
		s.setFechaFin(fin);

		HistoriaUsuario hu = new HistoriaUsuario();
		hu.setId("hu1");
		hu.setEstimacionPuntos(8);
		when(historiaUsuarioRepository.findBySprintId(sprintId)).thenReturn(List.of(hu));

		Tarea t1 = new Tarea();
		t1.setHistoriaId("hu1");
		t1.setSprintId(sprintId);
		t1.setEstado(EstadosTarea.COMPLETADA);
		t1.setFechaActualizacion(LocalDateTime.of(2026, 5, 2, 12, 0));

		Tarea t2 = new Tarea();
		t2.setHistoriaId("hu1");
		t2.setSprintId(sprintId);
		t2.setEstado(EstadosTarea.COMPLETADA);
		t2.setFechaActualizacion(LocalDateTime.of(2026, 5, 2, 14, 0));

		when(tareaRepository.findBySprintId(sprintId)).thenReturn(List.of(t1, t2));

		BurnChartService.SprintBurndown bd = burnChartService.buildSprintBurndown(s);

		assertThat(bd.getCommittedStoryPoints()).isEqualTo(8);
		assertThat(bd.getRemainingReal()).hasSize(3);
		// 1 may: aún no todas las tareas “cerradas” para ese día → 8 restantes
		assertThat(bd.getRemainingReal().get(0)).isEqualTo(8);
		// 2 may: historia completa
		assertThat(bd.getRemainingReal().get(1)).isZero();
		assertThat(bd.getRemainingReal().get(2)).isZero();
	}

	@Test
	void buildSprintBurndown_huSinTareasEnSprint_noReduceRestante() {
		String sprintId = "s2";
		LocalDate ini = LocalDate.of(2026, 5, 10);
		LocalDate fin = LocalDate.of(2026, 5, 11);

		Sprint s = new Sprint();
		s.setId(sprintId);
		s.setFechaInicio(ini);
		s.setFechaFin(fin);

		HistoriaUsuario hu = new HistoriaUsuario();
		hu.setId("hu2");
		hu.setEstimacionPuntos(5);
		hu.setEstado("ACEPTADA");
		hu.setFechaCreacion(LocalDateTime.of(2026, 5, 11, 9, 0));
		when(historiaUsuarioRepository.findBySprintId(sprintId)).thenReturn(List.of(hu));
		when(tareaRepository.findBySprintId(sprintId)).thenReturn(List.of());

		BurnChartService.SprintBurndown bd = burnChartService.buildSprintBurndown(s);

		assertThat(bd.getRemainingReal().get(0)).isEqualTo(5);
		assertThat(bd.getRemainingReal().get(1)).isEqualTo(5);
	}

	@Test
	void buildSprintBurndown_conBurnupProyecto_sinTareas_alineaRestanteConIncrementoDelBurnup() {
		String sprintId = "s5";
		LocalDate ini = LocalDate.of(2025, 3, 31);
		LocalDate fin = LocalDate.of(2025, 4, 11);
		Sprint s = new Sprint();
		s.setId(sprintId);
		s.setNumero(5);
		s.setFechaInicio(ini);
		s.setFechaFin(fin);

		HistoriaUsuario hu = new HistoriaUsuario();
		hu.setId("hu1");
		hu.setEstimacionPuntos(16);
		when(historiaUsuarioRepository.findBySprintId(sprintId)).thenReturn(List.of(hu));
		when(tareaRepository.findBySprintId(sprintId)).thenReturn(List.of());

		Proyecto p = new Proyecto();
		p.setBurnupAcumulado(Arrays.asList(0d, 42d, 80d, 125d, 165d, 200d, 235d, 251d));
		// Delta sprint 5: 200 - 165 = 35; alcance = max(16, 35) = 35

		BurnChartService.SprintBurndown bd = burnChartService.buildSprintBurndown(s, p);

		assertThat(bd.getCommittedStoryPoints()).isEqualTo(35);
		assertThat(bd.getRemainingReal().get(0)).isEqualTo(35.0);
		assertThat(bd.getRemainingReal().get(bd.getRemainingReal().size() - 1)).isZero();
	}

	@Test
	void burnupDeltaParaSprint_resuelveIndices() {
		Proyecto p = new Proyecto();
		p.setBurnupAcumulado(Arrays.asList(0d, 10d, 25d, 25d));
		assertThat(BurnChartService.burnupDeltaParaSprint(p, 1)).isEqualTo(10d);
		assertThat(BurnChartService.burnupDeltaParaSprint(p, 2)).isEqualTo(15d);
		assertThat(BurnChartService.burnupDeltaParaSprint(p, 3)).isZero();
	}

	@Test
	void elegirSprintDefecto_prefiereActivo() {
		Sprint a = new Sprint();
		a.setNumero(1);
		a.setEstado("FINALIZADO");
		Sprint b = new Sprint();
		b.setNumero(2);
		b.setEstado("ACTIVO");
		assertThat(BurnChartService.elegirSprintDefecto(List.of(a, b))).isSameAs(b);
	}
}
