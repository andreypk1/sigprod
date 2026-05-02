package com.sigpro.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sigpro.model.RolSistema;
import com.sigpro.model.Usuario;

public class UsuarioPrincipal implements UserDetails {

	private static final String ADMIN_DEMO_CORREO = "admin@sigprod.com";

	private final Usuario usuario;

	public UsuarioPrincipal(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	/** Rol efectivo para seguridad y UI (corrige null en BD para la cuenta admin demo). */
	public RolSistema getRolEfectivo() {
		RolSistema rol = usuario.getRol();
		if (rol == null && esAdminDemoCorreo()) {
			return RolSistema.ADMINISTRADOR;
		}
		return rol;
	}

	private boolean esAdminDemoCorreo() {
		String c = usuario.getCorreo();
		return c != null && ADMIN_DEMO_CORREO.equalsIgnoreCase(c.trim());
	}

	public String getId() {
		return usuario.getId();
	}

	public RolSistema getRolSistema() {
		return getRolEfectivo();
	}

	public String nombreCompleto() {
		return usuario.nombreCompleto().trim();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		RolSistema rol = usuario.getRol();
		if (rol == null && esAdminDemoCorreo()) {
			rol = RolSistema.ADMINISTRADOR;
		}
		if (rol == null) {
			return List.of(new SimpleGrantedAuthority("ROLE_" + RolSistema.DESARROLLADOR_BACKEND.name()));
		}
		/* El administrador debe pasar cualquier hasAnyRole(...) de módulos sin listar cada rol en todas las reglas. */
		if (rol == RolSistema.ADMINISTRADOR) {
			return Arrays.stream(RolSistema.values())
					.map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
					.collect(Collectors.toUnmodifiableList());
		}
		return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
	}

	@Override
	public String getPassword() {
		return usuario.getPasswordHash();
	}

	@Override
	public String getUsername() {
		return usuario.getCorreo();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return "ACTIVO".equalsIgnoreCase(usuario.getEstado());
	}
}
