package com.sigpro.model;

import java.time.LocalDate;

public class MiembroProyecto {

	private String usuarioId;
	private String usuarioNombre;
	private String correo;
	private RolSistema rolEnProyecto;
	private Integer porcentajeDedicacion;
	private Double tarifaHora;
	private LocalDate fechaAsignacion;

	public MiembroProyecto() {
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

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public RolSistema getRolEnProyecto() {
		return rolEnProyecto;
	}

	public void setRolEnProyecto(RolSistema rolEnProyecto) {
		this.rolEnProyecto = rolEnProyecto;
	}

	public Integer getPorcentajeDedicacion() {
		return porcentajeDedicacion;
	}

	public void setPorcentajeDedicacion(Integer porcentajeDedicacion) {
		this.porcentajeDedicacion = porcentajeDedicacion;
	}

	public Double getTarifaHora() {
		return tarifaHora;
	}

	public void setTarifaHora(Double tarifaHora) {
		this.tarifaHora = tarifaHora;
	}

	public LocalDate getFechaAsignacion() {
		return fechaAsignacion;
	}

	public void setFechaAsignacion(LocalDate fechaAsignacion) {
		this.fechaAsignacion = fechaAsignacion;
	}
}
