package com.sigpro.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notificaciones")
public class Notificacion {

	@Id
	private String id;

	private String usuarioDestinoId;
	private String titulo;
	private String mensaje;
	private String tipo;
	private Boolean leida;
	private LocalDateTime fecha;

	public Notificacion() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsuarioDestinoId() {
		return usuarioDestinoId;
	}

	public void setUsuarioDestinoId(String usuarioDestinoId) {
		this.usuarioDestinoId = usuarioDestinoId;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Boolean getLeida() {
		return leida;
	}

	public void setLeida(Boolean leida) {
		this.leida = leida;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
}
