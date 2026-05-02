package com.sigpro.dto;

import java.time.LocalDate;

public class TareaApiDto {

	private String titulo;
	private String descripcion;
	private String historiaId;
	private String proyectoId;
	private String sprintId;
	private String tipo;
	private String asignadoId;
	private Double horasEstimadas;
	private Double horasReales;
	private String estado;
	private String prioridad;
	private LocalDate fechaLimite;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getHistoriaId() {
		return historiaId;
	}

	public void setHistoriaId(String historiaId) {
		this.historiaId = historiaId;
	}

	public String getProyectoId() {
		return proyectoId;
	}

	public void setProyectoId(String proyectoId) {
		this.proyectoId = proyectoId;
	}

	public String getSprintId() {
		return sprintId;
	}

	public void setSprintId(String sprintId) {
		this.sprintId = sprintId;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getAsignadoId() {
		return asignadoId;
	}

	public void setAsignadoId(String asignadoId) {
		this.asignadoId = asignadoId;
	}

	public Double getHorasEstimadas() {
		return horasEstimadas;
	}

	public void setHorasEstimadas(Double horasEstimadas) {
		this.horasEstimadas = horasEstimadas;
	}

	public Double getHorasReales() {
		return horasReales;
	}

	public void setHorasReales(Double horasReales) {
		this.horasReales = horasReales;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(String prioridad) {
		this.prioridad = prioridad;
	}

	public LocalDate getFechaLimite() {
		return fechaLimite;
	}

	public void setFechaLimite(LocalDate fechaLimite) {
		this.fechaLimite = fechaLimite;
	}
}
