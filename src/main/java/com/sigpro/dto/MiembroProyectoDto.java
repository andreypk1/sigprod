package com.sigpro.dto;

import com.sigpro.model.RolSistema;

public class MiembroProyectoDto {

	private String usuarioId;
	private String usuarioNombre;
	private String correo;
	private RolSistema rolEnProyecto;
	private Integer porcentajeDedicacion;
	private Double tarifaHora;

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
}
