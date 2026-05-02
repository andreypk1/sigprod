package com.sigpro.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "despliegues")
public class Despliegue {

	@Id
	private String id;
	private String pipelineId;
	private String ambienteId;
	private String proyectoId;
	private String version;
	private String estrategia;
	private String estado;
	private String ejecutorId;
	private LocalDateTime fecha;
	private String notasDespliegue;

	public Despliegue() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPipelineId() {
		return pipelineId;
	}

	public void setPipelineId(String pipelineId) {
		this.pipelineId = pipelineId;
	}

	public String getAmbienteId() {
		return ambienteId;
	}

	public void setAmbienteId(String ambienteId) {
		this.ambienteId = ambienteId;
	}

	public String getProyectoId() {
		return proyectoId;
	}

	public void setProyectoId(String proyectoId) {
		this.proyectoId = proyectoId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getEstrategia() {
		return estrategia;
	}

	public void setEstrategia(String estrategia) {
		this.estrategia = estrategia;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEjecutorId() {
		return ejecutorId;
	}

	public void setEjecutorId(String ejecutorId) {
		this.ejecutorId = ejecutorId;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public String getNotasDespliegue() {
		return notasDespliegue;
	}

	public void setNotasDespliegue(String notasDespliegue) {
		this.notasDespliegue = notasDespliegue;
	}
}
