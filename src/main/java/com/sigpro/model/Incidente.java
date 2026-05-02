package com.sigpro.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "incidentes")
public class Incidente {

	@Id
	private String id;
	private String proyectoId;
	private String titulo;
	private String descripcion;
	private String severidad;
	private String estado;
	private LocalDateTime fechaReporte;
	private LocalDateTime fechaResolucion;
	private String causaRaiz;
	private String accionesCorrectivas;
	private String responsableId;

	public Incidente() {
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

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public LocalDateTime getFechaReporte() {
		return fechaReporte;
	}

	public void setFechaReporte(LocalDateTime fechaReporte) {
		this.fechaReporte = fechaReporte;
	}

	public LocalDateTime getFechaResolucion() {
		return fechaResolucion;
	}

	public void setFechaResolucion(LocalDateTime fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}

	public String getCausaRaiz() {
		return causaRaiz;
	}

	public void setCausaRaiz(String causaRaiz) {
		this.causaRaiz = causaRaiz;
	}

	public String getAccionesCorrectivas() {
		return accionesCorrectivas;
	}

	public void setAccionesCorrectivas(String accionesCorrectivas) {
		this.accionesCorrectivas = accionesCorrectivas;
	}

	public String getResponsableId() {
		return responsableId;
	}

	public void setResponsableId(String responsableId) {
		this.responsableId = responsableId;
	}
}
