package com.sigpro.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "riesgos")
public class Riesgo {

	@Id
	private String id;

	private String proyectoId;
	private String descripcion;
	private Integer probabilidad;
	private Integer impacto;
	private String planMitigacion;
	private String responsableId;
	private String estado;

	public Riesgo() {
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getProbabilidad() {
		return probabilidad;
	}

	public void setProbabilidad(Integer probabilidad) {
		this.probabilidad = probabilidad;
	}

	public Integer getImpacto() {
		return impacto;
	}

	public void setImpacto(Integer impacto) {
		this.impacto = impacto;
	}

	public String getPlanMitigacion() {
		return planMitigacion;
	}

	public void setPlanMitigacion(String planMitigacion) {
		this.planMitigacion = planMitigacion;
	}

	public String getResponsableId() {
		return responsableId;
	}

	public void setResponsableId(String responsableId) {
		this.responsableId = responsableId;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
