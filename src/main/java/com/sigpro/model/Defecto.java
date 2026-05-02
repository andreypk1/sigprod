package com.sigpro.model;

import jakarta.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "defectos")
public class Defecto {

	@Id
	private String id;

	private String proyectoId;
	private String tareaId;

	@NotBlank
	private String titulo;

	private String descripcion;
	private String severidad;
	private String prioridad;
	private String estado;
	private String reportadoPorId;
	private String asignadoAId;
	private String ambiente;
	private String evidenciaUrl;
	private LocalDateTime fechaReporte;

	public Defecto() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProyectoId() {
		return proyectoId;
	}

	public void setProyectoId(String proyectoId) {
		this.proyectoId = proyectoId;
	}

	public String getTareaId() {
		return tareaId;
	}

	public void setTareaId(String tareaId) {
		this.tareaId = tareaId;
	}

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

	public String getSeveridad() {
		return severidad;
	}

	public void setSeveridad(String severidad) {
		this.severidad = severidad;
	}

	public String getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(String prioridad) {
		this.prioridad = prioridad;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getReportadoPorId() {
		return reportadoPorId;
	}

	public void setReportadoPorId(String reportadoPorId) {
		this.reportadoPorId = reportadoPorId;
	}

	public String getAsignadoAId() {
		return asignadoAId;
	}

	public void setAsignadoAId(String asignadoAId) {
		this.asignadoAId = asignadoAId;
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public String getEvidenciaUrl() {
		return evidenciaUrl;
	}

	public void setEvidenciaUrl(String evidenciaUrl) {
		this.evidenciaUrl = evidenciaUrl;
	}

	public LocalDateTime getFechaReporte() {
		return fechaReporte;
	}

	public void setFechaReporte(LocalDateTime fechaReporte) {
		this.fechaReporte = fechaReporte;
	}
}
