package com.sigpro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.sigpro.model.RolSistema;

public class RegistroDto {

	@NotBlank
	private String nombres;

	@NotBlank
	private String apellidos;

	@Email
	@NotBlank
	private String correo;

	@NotBlank
	@Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
	private String password;

	@NotBlank
	private String confirmarPassword;

	private String telefono;

	@NotNull
	private RolSistema rol;

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmarPassword() {
		return confirmarPassword;
	}

	public void setConfirmarPassword(String confirmarPassword) {
		this.confirmarPassword = confirmarPassword;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public RolSistema getRol() {
		return rol;
	}

	public void setRol(RolSistema rol) {
		this.rol = rol;
	}
}
