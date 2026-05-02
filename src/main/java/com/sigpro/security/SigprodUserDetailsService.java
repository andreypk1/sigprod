package com.sigpro.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sigpro.model.Usuario;
import com.sigpro.repository.UsuarioRepository;

@Service
public class SigprodUserDetailsService implements UserDetailsService {

	private final UsuarioRepository usuarioRepository;

	public SigprodUserDetailsService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
		return new UsuarioPrincipal(usuario);
	}
}
