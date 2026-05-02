package com.sigpro.model;

import jakarta.validation.constraints.NotBlank;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "historias_usuario")
public class HistoriaUsuario {

	@Id
	private String id;

	private String epicaId;
	private String proyectoId;

	@NotBlank
	private String titulo;

	private String narrativa;
	private String prioridadMoscow;
	private Integer estimacionPuntos;
	private Integer valorNegocio;
	private String estado;
	private String sprintId;

	private List<CriterioAceptacion> criteriosAceptacion = new ArrayList<>();

	@CreatedDate
	private LocalDateTime fechaCreacion;

	public HistoriaUsuario() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEpicaId() {
		return epicaId;
	}

	public void setEpicaId(String epicaId) {
		this.epicaId = epicaId;
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

	public String getNarrativa() {
		return narrativa;
	}

	public void setNarrativa(String narrativa) {
		this.narrativa = narrativa;
	}

	public String getPrioridadMoscow() {
		return prioridadMoscow;
	}

	public void setPrioridadMoscow(String prioridadMoscow) {
		this.prioridadMoscow = prioridadMoscow;
	}

	public Integer getEstimacionPuntos() {
		return estimacionPuntos;
	}

	public void setEstimacionPuntos(Integer estimacionPuntos) {
		this.estimacionPuntos = estimacionPuntos;
	}

	public Integer getValorNegocio() {
		return valorNegocio;
	}

	public void setValorNegocio(Integer valorNegocio) {
		this.valorNegocio = valorNegocio;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getSprintId() {
		return sprintId;
	}

	public void setSprintId(String sprintId) {
		this.sprintId = sprintId;
	}

	public List<CriterioAceptacion> getCriteriosAceptacion() {
		return criteriosAceptacion;
	}

	public void setCriteriosAceptacion(List<CriterioAceptacion> criteriosAceptacion) {
		this.criteriosAceptacion = criteriosAceptacion != null ? criteriosAceptacion : new ArrayList<>();
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
}
