package com.sigpro.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "planes_prueba")
public class PlanPruebas {

	@Id
	private String id;
	private String proyectoId;
	private String sprintId;
	private String nombre;
	private String alcance;
	private String estrategia;
	private String criteriosEntrada;
	private String criteriosSalida;
	private String estado;
	private String qaId;

	@CreatedDate
	private LocalDateTime fechaCreacion;

	public PlanPruebas() {
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

	public String getSprintId() {
		return sprintId;
	}

	public void setSprintId(String sprintId) {
		this.sprintId = sprintId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getAlcance() {
		return alcance;
	}

	public void setAlcance(String alcance) {
		this.alcance = alcance;
	}

	public String getEstrategia() {
		return estrategia;
	}

	public void setEstrategia(String estrategia) {
		this.estrategia = estrategia;
	}

	public String getCriteriosEntrada() {
		return criteriosEntrada;
	}

	public void setCriteriosEntrada(String criteriosEntrada) {
		this.criteriosEntrada = criteriosEntrada;
	}

	public String getCriteriosSalida() {
		return criteriosSalida;
	}

	public void setCriteriosSalida(String criteriosSalida) {
		this.criteriosSalida = criteriosSalida;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getQaId() {
		return qaId;
	}

	public void setQaId(String qaId) {
		this.qaId = qaId;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
}
