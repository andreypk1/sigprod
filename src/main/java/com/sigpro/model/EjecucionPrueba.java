package com.sigpro.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "ejecuciones_prueba")
public class EjecucionPrueba {

	@Id
	private String id;
	private String casoPruebaId;
	private String proyectoId;
	private String sprintId;
	private String testerId;
	private String resultado;
	private String ambiente;
	private String evidenciaUrl;
	private String comentarios;
	private LocalDateTime fecha;

	public EjecucionPrueba() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCasoPruebaId() {
		return casoPruebaId;
	}

	public void setCasoPruebaId(String casoPruebaId) {
		this.casoPruebaId = casoPruebaId;
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

	public String getTesterId() {
		return testerId;
	}

	public void setTesterId(String testerId) {
		this.testerId = testerId;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
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

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
}
