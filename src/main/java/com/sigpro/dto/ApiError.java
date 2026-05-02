package com.sigpro.dto;

public class ApiError {

	private String mensaje;
	private String codigo;

	public ApiError() {
	}

	public ApiError(String mensaje, String codigo) {
		this.mensaje = mensaje;
		this.codigo = codigo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
}
