package com.sigpro.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;

public class ProyectoFormDto {

	@NotBlank
	private String nombre;

	private String descripcion;

	@NotBlank
	private String clienteId;

	private String metodologia;

	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private Double presupuesto;

	private String productOwnerId;
	private String projectManagerId;

	private List<MiembroProyectoDto> miembros = new ArrayList<>();

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getClienteId() {
		return clienteId;
	}

	public void setClienteId(String clienteId) {
		this.clienteId = clienteId;
	}

	public String getMetodologia() {
		return metodologia;
	}

	public void setMetodologia(String metodologia) {
		this.metodologia = metodologia;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Double getPresupuesto() {
		return presupuesto;
	}

	public void setPresupuesto(Double presupuesto) {
		this.presupuesto = presupuesto;
	}

	public String getProductOwnerId() {
		return productOwnerId;
	}

	public void setProductOwnerId(String productOwnerId) {
		this.productOwnerId = productOwnerId;
	}

	public String getProjectManagerId() {
		return projectManagerId;
	}

	public void setProjectManagerId(String projectManagerId) {
		this.projectManagerId = projectManagerId;
	}

	public List<MiembroProyectoDto> getMiembros() {
		return miembros;
	}

	public void setMiembros(List<MiembroProyectoDto> miembros) {
		this.miembros = miembros != null ? miembros : new ArrayList<>();
	}
}
