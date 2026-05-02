package com.sigpro.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "pipelines_cicd")
public class PipelineCicd {

	@Id
	private String id;
	private String proyectoId;
	private String nombre;
	private String repositorioUrl;
	private String rama;
	private String etapasJson;
	private String estadoUltimaEjecucion;
	private LocalDateTime ultimaEjecucionFecha;

	@CreatedDate
	private LocalDateTime fechaCreacion;

	public PipelineCicd() {
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

	public String getRepositorioUrl() {
		return repositorioUrl;
	}

	public void setRepositorioUrl(String repositorioUrl) {
		this.repositorioUrl = repositorioUrl;
	}

	public String getRama() {
		return rama;
	}

	public void setRama(String rama) {
		this.rama = rama;
	}

	public String getEtapasJson() {
		return etapasJson;
	}

	public void setEtapasJson(String etapasJson) {
		this.etapasJson = etapasJson;
	}

	public String getEstadoUltimaEjecucion() {
		return estadoUltimaEjecucion;
	}

	public void setEstadoUltimaEjecucion(String estadoUltimaEjecucion) {
		this.estadoUltimaEjecucion = estadoUltimaEjecucion;
	}

	public LocalDateTime getUltimaEjecucionFecha() {
		return ultimaEjecucionFecha;
	}

	public void setUltimaEjecucionFecha(LocalDateTime ultimaEjecucionFecha) {
		this.ultimaEjecucionFecha = ultimaEjecucionFecha;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
}
