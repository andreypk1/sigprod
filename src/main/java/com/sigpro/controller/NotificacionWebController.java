package com.sigpro.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sigpro.repository.NotificacionRepository;
import com.sigpro.security.UsuarioPrincipal;

@Controller
@RequestMapping("/notificaciones")
public class NotificacionWebController {

	private final NotificacionRepository notificacionRepository;

	public NotificacionWebController(NotificacionRepository notificacionRepository) {
		this.notificacionRepository = notificacionRepository;
	}

	@GetMapping({"", "/"})
	public String lista(@AuthenticationPrincipal UsuarioPrincipal principal, Model model) {
		model.addAttribute("items",
				notificacionRepository.findByUsuarioDestinoIdOrderByFechaDesc(principal.getId()));
		return "modulos/notificaciones";
	}
}
