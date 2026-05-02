package com.sigpro.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sigpro.model.HistoriaUsuario;
import com.sigpro.model.Proyecto;
import com.sigpro.model.Tarea;
import com.sigpro.repository.HistoriaUsuarioRepository;
import com.sigpro.repository.ProyectoRepository;
import com.sigpro.repository.TareaRepository;

@Controller
@RequestMapping("/exportar")
public class ExportacionController {

	private final ProyectoRepository proyectoRepository;
	private final TareaRepository tareaRepository;
	private final HistoriaUsuarioRepository historiaUsuarioRepository;

	public ExportacionController(ProyectoRepository proyectoRepository, TareaRepository tareaRepository,
			HistoriaUsuarioRepository historiaUsuarioRepository) {
		this.proyectoRepository = proyectoRepository;
		this.tareaRepository = tareaRepository;
		this.historiaUsuarioRepository = historiaUsuarioRepository;
	}

	@GetMapping("/proyectos/csv")
	public void exportarProyectosCsv(HttpServletResponse response) throws IOException {
		response.setContentType("text/csv;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"proyectos.csv\"");
		PrintWriter writer = response.getWriter();
		writer.println("Nombre,Cliente,Metodología,Estado,Presupuesto,Inicio,Fin,Product Owner,Project Manager");
		for (Proyecto p : proyectoRepository.findAll()) {
			writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",%s,\"%s\",\"%s\",\"%s\",\"%s\"%n",
					safe(p.getNombre()), safe(p.getClienteNombre()),
					safe(p.getMetodologia()), safe(p.getEstado()),
					p.getPresupuesto() != null ? p.getPresupuesto() : "",
					p.getFechaInicio() != null ? p.getFechaInicio() : "",
					p.getFechaFin() != null ? p.getFechaFin() : "",
					safe(p.getProductOwnerNombre()), safe(p.getProjectManagerNombre()));
		}
		writer.flush();
	}

	@GetMapping("/tareas/{proyectoId}/csv")
	public void exportarTareasCsv(@PathVariable String proyectoId, HttpServletResponse response) throws IOException {
		response.setContentType("text/csv;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"tareas.csv\"");
		PrintWriter writer = response.getWriter();
		writer.println("Título,Tipo,Asignado,Estado,Prioridad,Horas Est.,Horas Reales,Fecha Límite");
		for (Tarea t : tareaRepository.findByProyectoId(proyectoId)) {
			writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%s,%s,\"%s\"%n",
					safe(t.getTitulo()), safe(t.getTipo()), safe(t.getAsignadoNombre()),
					safe(t.getEstado()), safe(t.getPrioridad()),
					t.getHorasEstimadas() != null ? t.getHorasEstimadas() : "",
					t.getHorasReales() != null ? t.getHorasReales() : "",
					t.getFechaLimite() != null ? t.getFechaLimite() : "");
		}
		writer.flush();
	}

	@GetMapping("/historias/{proyectoId}/csv")
	public void exportarHistoriasCsv(@PathVariable String proyectoId, HttpServletResponse response) throws IOException {
		response.setContentType("text/csv;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"historias.csv\"");
		PrintWriter writer = response.getWriter();
		writer.println("Título,Narrativa,MoSCoW,Puntos,Valor,Estado,Sprint");
		for (HistoriaUsuario h : historiaUsuarioRepository.findByProyectoId(proyectoId)) {
			writer.printf("\"%s\",\"%s\",\"%s\",%s,%s,\"%s\",\"%s\"%n",
					safe(h.getTitulo()), safe(h.getNarrativa()), safe(h.getPrioridadMoscow()),
					h.getEstimacionPuntos() != null ? h.getEstimacionPuntos() : "",
					h.getValorNegocio() != null ? h.getValorNegocio() : "",
					safe(h.getEstado()), safe(h.getSprintId()));
		}
		writer.flush();
	}

	private static String safe(String s) {
		return s != null ? s.replace("\"", "\"\"") : "";
	}
}
