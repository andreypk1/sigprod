package com.sigpro.model;

import jakarta.validation.constraints.NotBlank;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "tareas")
public class Tarea {

	@Id
	private String id;

	private String historiaId;
	private String proyectoId;
	private String sprintId;

	@NotBlank
	private String titulo;

	private String descripcion;
	private String tipo;
	private String asignadoId;
	private String asignadoNombre;
	private Double horasEstimadas;
	private Double horasReales;
	private String estado;
	private String prioridad;
	private LocalDate fechaLimite;

	@CreatedDate
	private LocalDateTime fechaCreacion;

	@LastModifiedDate
	private LocalDateTime fechaActualizacion;

	public Tarea() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getAsignadoNombre() {
		return asignadoNombre;
	}

	public void setAsignadoNombre(String asignadoNombre) {
		this.asignadoNombre = asignadoNombre;
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

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public LocalDateTime getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}
}
