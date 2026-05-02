package com.sigpro.controller;

import java.util.Arrays;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sigpro.dto.RegistroDto;
import com.sigpro.security.UsuarioPrincipal;
import com.sigpro.model.RolSistema;
import com.sigpro.service.BusinessRuleException;
import com.sigpro.service.UsuarioService;

@Controller
public class AuthController {

	private final UsuarioService usuarioService;

	public AuthController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	/** Roles que el formulario público de registro puede ofrecer (sin administrador). */
	private static List<RolSistema> rolesRegistroPublico() {
		return Arrays.stream(RolSistema.values()).filter(r -> r != RolSistema.ADMINISTRADOR).toList();
	}

	@GetMapping("/login")
	public String login(@AuthenticationPrincipal UsuarioPrincipal principal) {
		if (principal != null) {
			return "redirect:/dashboard";
		}
		return "auth/login";
	}

	@GetMapping("/registro")
	public String registroForm(Model model) {
		if (!model.containsAttribute("registro")) {
			model.addAttribute("registro", new RegistroDto());
		}
		model.addAttribute("roles", rolesRegistroPublico());
		return "auth/registro";
	}

	@PostMapping("/registro")
	public String registro(@Valid @ModelAttribute("registro") RegistroDto dto, BindingResult bindingResult,
			Model model, RedirectAttributes redirectAttributes) {
		model.addAttribute("roles", rolesRegistroPublico());
		if (bindingResult.hasErrors()) {
			return "auth/registro";
		}
		if (dto.getRol() == RolSistema.ADMINISTRADOR) {
			bindingResult.reject("global", "El rol de administrador no está disponible en el registro público.");
			return "auth/registro";
		}
		if (!dto.getPassword().equals(dto.getConfirmarPassword())) {
			bindingResult.rejectValue("confirmarPassword", "match", "Las contraseñas no coinciden.");
			return "auth/registro";
		}
		if (usuarioService.existeCorreo(dto.getCorreo())) {
			bindingResult.rejectValue("correo", "unique",
					"Este correo ya está registrado. ¿Deseas iniciar sesión?");
			return "auth/registro";
		}
		try {
			usuarioService.registrar(dto);
		} catch (BusinessRuleException ex) {
			bindingResult.reject("global", ex.getMessage());
			return "auth/registro";
		}
		return "redirect:/login?registroExitoso";
	}
}
