package com.sigpro.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sigpro.model.Usuario;
import com.sigpro.repository.UsuarioRepository;
import com.sigpro.security.UsuarioPrincipal;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	public PerfilController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping("/cambiar-password")
	public String cambiarPasswordForm() {
		return "perfil/cambiar-password";
	}

	@PostMapping("/cambiar-password")
	public String cambiarPassword(@RequestParam String passwordActual, @RequestParam String passwordNueva,
			@RequestParam String confirmarPassword, @AuthenticationPrincipal UsuarioPrincipal principal,
			RedirectAttributes ra) {

		Usuario u = usuarioRepository.findById(principal.getId()).orElseThrow();

		if (!passwordEncoder.matches(passwordActual, u.getPasswordHash())) {
			ra.addFlashAttribute("error", "La contraseña actual no es correcta.");
			return "redirect:/perfil/cambiar-password";
		}
		if (!passwordNueva.equals(confirmarPassword)) {
			ra.addFlashAttribute("error", "La nueva contraseña y su confirmación no coinciden.");
			return "redirect:/perfil/cambiar-password";
		}
		if (passwordNueva.length() < 8) {
			ra.addFlashAttribute("error", "La nueva contraseña debe tener al menos 8 caracteres.");
			return "redirect:/perfil/cambiar-password";
		}
		u.setPasswordHash(passwordEncoder.encode(passwordNueva));
		usuarioRepository.save(u);
		ra.addFlashAttribute("exito", "Contraseña actualizada correctamente.");
		return "redirect:/perfil/cambiar-password";
	}
}
