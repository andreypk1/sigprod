package com.sigpro.model;

import jakarta.validation.constraints.NotBlank;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Document(collection = "proyectos")
public class Proyecto {

	@Id
	private String id;

	@NotBlank
	private String nombre;

	private String descripcion;
	private String clienteId;
	private String clienteNombre;
	private String productOwnerId;
	private String productOwnerNombre;
	private String projectManagerId;
	private String projectManagerNombre;

	private List<MiembroProyecto> miembros = new ArrayList<>();

	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private Double presupuesto;
	private String metodologia;
	private String estado;

	/** Suma de story points del producto (backlog + entregado); se usa en burn charts. */
	private Integer totalPuntosProducto;

	/** Progreso mostrado en dashboard/detalle (0–100). Si es null, se calcula por tareas completadas. */
	private Integer progresoPorcentaje;

	/**
	 * Series predefinidas para gráficos (se rellenan en demo/seed). Si están vacías, se calculan desde historias.
	 */
	private List<Double> burnupAcumulado;
	private List<Double> burndownRestante;
	private List<String> etiquetasBurnSprints;

	@CreatedDate
	private LocalDateTime fechaCreacion;

	@LastModifiedDate
	private LocalDateTime fechaActualizacion;

	public Proyecto() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getClienteNombre() {
		return clienteNombre;
	}

	public void setClienteNombre(String clienteNombre) {
		this.clienteNombre = clienteNombre;
	}

	public String getProductOwnerId() {
		return productOwnerId;
	}

	public void setProductOwnerId(String productOwnerId) {
		this.productOwnerId = productOwnerId;
	}

	public String getProductOwnerNombre() {
		return productOwnerNombre;
	}

	public void setProductOwnerNombre(String productOwnerNombre) {
		this.productOwnerNombre = productOwnerNombre;
	}

	public String getProjectManagerId() {
		return projectManagerId;
	}

	public void setProjectManagerId(String projectManagerId) {
		this.projectManagerId = projectManagerId;
	}

	public String getProjectManagerNombre() {
		return projectManagerNombre;
	}

	public void setProjectManagerNombre(String projectManagerNombre) {
		this.projectManagerNombre = projectManagerNombre;
	}

	public List<MiembroProyecto> getMiembros() {
		return miembros;
	}

	public void setMiembros(List<MiembroProyecto> miembros) {
		this.miembros = miembros != null ? miembros : new ArrayList<>();
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

	public String getMetodologia() {
		return metodologia;
	}

	public void setMetodologia(String metodologia) {
		this.metodologia = metodologia;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Integer getTotalPuntosProducto() {
		return totalPuntosProducto;
	}

	public void setTotalPuntosProducto(Integer totalPuntosProducto) {
		this.totalPuntosProducto = totalPuntosProducto;
	}

	/** Story points totales (alias para vistas y burn charts). */
	public int getTotalPuntos() {
		return totalPuntosProducto != null ? totalPuntosProducto : 0;
	}

	public Integer getProgresoPorcentaje() {
		return progresoPorcentaje;
	}

	public void setProgresoPorcentaje(Integer progresoPorcentaje) {
		this.progresoPorcentaje = progresoPorcentaje;
	}

	public List<Double> getBurnupAcumulado() {
		return burnupAcumulado != null ? burnupAcumulado : Collections.emptyList();
	}

	public void setBurnupAcumulado(List<Double> burnupAcumulado) {
		this.burnupAcumulado = burnupAcumulado;
	}

	public List<Double> getBurndownRestante() {
		return burndownRestante != null ? burndownRestante : Collections.emptyList();
	}

	public void setBurndownRestante(List<Double> burndownRestante) {
		this.burndownRestante = burndownRestante;
	}

	public List<String> getEtiquetasBurnSprints() {
		return etiquetasBurnSprints != null ? etiquetasBurnSprints : Collections.emptyList();
	}

	public void setEtiquetasBurnSprints(List<String> etiquetasBurnSprints) {
		this.etiquetasBurnSprints = etiquetasBurnSprints;
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
