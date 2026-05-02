package com.sigpro.controller.advice;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.sigpro.security.UsuarioPrincipal;
import com.sigpro.service.NotificacionService;

@ControllerAdvice
public class GlobalModelAdvice {

	private final NotificacionService notificacionService;

	public GlobalModelAdvice(NotificacionService notificacionService) {
		this.notificacionService = notificacionService;
	}

	@ModelAttribute
	public void globalUsuario(Authentication auth,
			org.springframework.ui.Model model) {
		if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UsuarioPrincipal) {
			UsuarioPrincipal up = (UsuarioPrincipal) auth.getPrincipal();
			model.addAttribute("nombreUsuario", up.nombreCompleto());
			String nombre = up.nombreCompleto().trim();
			String ini = nombre.isEmpty() ? "U"
					: (nombre.length() >= 2 ? nombre.substring(0, 2).toUpperCase()
							: nombre.substring(0, 1).toUpperCase());
			model.addAttribute("inicialesUsuario", ini);
			model.addAttribute("notificacionesNoLeidas", notificacionService.contarNoLeidas(up.getId()));
		}
	}
}
