package com.sigpro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigpro.dto.RegistroDto;
import com.sigpro.model.RolSistema;
import com.sigpro.model.Usuario;
import com.sigpro.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public Optional<Usuario> findById(String id) {
		return usuarioRepository.findById(id);
	}

	public Optional<Usuario> findByCorreo(String correo) {
		return usuarioRepository.findByCorreoIgnoreCase(correo);
	}

	public boolean existeCorreo(String correo) {
		return usuarioRepository.existsByCorreoIgnoreCase(correo);
	}

	public List<Usuario> listarTodos() {
		return usuarioRepository.findAll();
	}

	public List<Usuario> listarPorRol(RolSistema rol) {
		return usuarioRepository.findByRol(rol);
	}

	@Transactional
	public Usuario registrar(RegistroDto dto) {
		if (dto.getRol() == RolSistema.ADMINISTRADOR) {
			throw new BusinessRuleException("El rol de administrador no está permitido en el registro.");
		}
		Usuario u = new Usuario();
		u.setNombres(dto.getNombres());
		u.setApellidos(dto.getApellidos());
		u.setCorreo(dto.getCorreo().trim().toLowerCase());
		u.setTelefono(dto.getTelefono());
		u.setRol(dto.getRol());
		u.setEstado("ACTIVO");
		u.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
		return usuarioRepository.save(u);
	}

	@Transactional
	public Usuario actualizarRolYEstado(String id, RolSistema rol, String estado) {
		Usuario u = usuarioRepository.findById(id).orElseThrow();
		u.setRol(rol);
		u.setEstado(estado);
		return usuarioRepository.save(u);
	}
}
