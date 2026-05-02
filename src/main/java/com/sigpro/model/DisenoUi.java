package com.sigpro.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "disenos_ui")
public class DisenoUi {

	@Id
	private String id;
	private String proyectoId;
	private String nombre;
	private String tipo;
	private String urlFigma;
	private String estado;
	private String disenadorId;
	private String descripcion;

	@CreatedDate
	private LocalDateTime fechaCreacion;

	public DisenoUi() {
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getUrlFigma() {
		return urlFigma;
	}

	public void setUrlFigma(String urlFigma) {
		this.urlFigma = urlFigma;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getDisenadorId() {
		return disenadorId;
	}

	public void setDisenadorId(String disenadorId) {
		this.disenadorId = disenadorId;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
}
