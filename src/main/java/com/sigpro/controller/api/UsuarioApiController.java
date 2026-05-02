package com.sigpro.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sigpro.model.RolSistema;
import com.sigpro.model.Usuario;
import com.sigpro.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("isAuthenticated()")
public class UsuarioApiController {

	private final UsuarioRepository usuarioRepository;

	public UsuarioApiController(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@GetMapping
	public List<Usuario> listar(@RequestParam(required = false) RolSistema rol) {
		if (rol != null) {
			return usuarioRepository.findByRol(rol);
		}
		return usuarioRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Usuario> detalle(@PathVariable String id) {
		return usuarioRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
}
