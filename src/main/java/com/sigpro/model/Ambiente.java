package com.sigpro.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "ambientes")
public class Ambiente {

	@Id
	private String id;
	private String proyectoId;
	private String nombre;
	private String url;
	private String configuracionJson;
	private String estado;
	private LocalDateTime ultimoDespliegue;

	public Ambiente() {
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getConfiguracionJson() {
		return configuracionJson;
	}

	public void setConfiguracionJson(String configuracionJson) {
		this.configuracionJson = configuracionJson;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public LocalDateTime getUltimoDespliegue() {
		return ultimoDespliegue;
	}

	public void setUltimoDespliegue(LocalDateTime ultimoDespliegue) {
		this.ultimoDespliegue = ultimoDespliegue;
	}
}
