package com.sigpro.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "bitacora_auditoria")
public class BitacoraAuditoria {

	@Id
	private String id;

	private String usuarioId;
	private String usuarioNombre;
	private String accion;
	private String entidad;
	private String entidadId;
	private String datosAnteriores;
	private String datosNuevos;
	private LocalDateTime fecha;
	private String ip;

	public BitacoraAuditoria() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(String usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getUsuarioNombre() {
		return usuarioNombre;
	}

	public void setUsuarioNombre(String usuarioNombre) {
		this.usuarioNombre = usuarioNombre;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getEntidad() {
		return entidad;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	public String getEntidadId() {
		return entidadId;
	}

	public void setEntidadId(String entidadId) {
		this.entidadId = entidadId;
	}

	public String getDatosAnteriores() {
		return datosAnteriores;
	}

	public void setDatosAnteriores(String datosAnteriores) {
		this.datosAnteriores = datosAnteriores;
	}

	public String getDatosNuevos() {
		return datosNuevos;
	}

	public void setDatosNuevos(String datosNuevos) {
		this.datosNuevos = datosNuevos;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
