package com.sigpro.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "endpoints_api")
public class EndpointApi {

	@Id
	private String id;
	private String proyectoId;
	private String metodoHttp;
	private String ruta;
	private String descripcion;
	private String requestSchema;
	private String responseSchema;
	private List<String> codigosRespuesta = new ArrayList<>();
	private String version;
	private String autorId;

	@CreatedDate
	private LocalDateTime fechaCreacion;

	public EndpointApi() {
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

	public String getMetodoHttp() {
		return metodoHttp;
	}

	public void setMetodoHttp(String metodoHttp) {
		this.metodoHttp = metodoHttp;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getRequestSchema() {
		return requestSchema;
	}

	public void setRequestSchema(String requestSchema) {
		this.requestSchema = requestSchema;
	}

	public String getResponseSchema() {
		return responseSchema;
	}

	public void setResponseSchema(String responseSchema) {
		this.responseSchema = responseSchema;
	}

	public List<String> getCodigosRespuesta() {
		return codigosRespuesta;
	}

	public void setCodigosRespuesta(List<String> codigosRespuesta) {
		this.codigosRespuesta = codigosRespuesta != null ? codigosRespuesta : new ArrayList<>();
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAutorId() {
		return autorId;
	}

	public void setAutorId(String autorId) {
		this.autorId = autorId;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
}
