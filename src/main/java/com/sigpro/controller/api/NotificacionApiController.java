package com.sigpro.controller.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sigpro.model.Notificacion;
import com.sigpro.security.UsuarioPrincipal;
import com.sigpro.service.BusinessRuleException;
import com.sigpro.service.NotificacionService;

@RestController
@RequestMapping("/api/notificaciones")
@PreAuthorize("isAuthenticated()")
public class NotificacionApiController {

	private final NotificacionService notificacionService;

	public NotificacionApiController(NotificacionService notificacionService) {
		this.notificacionService = notificacionService;
	}

	@GetMapping("/mis-notificaciones")
	public java.util.List<Notificacion> misNotificaciones(@AuthenticationPrincipal UsuarioPrincipal principal) {
		return notificacionService.ultimasNoLeidas(principal.getId());
	}

	@GetMapping("/no-leidas-count")
	@ResponseBody
	public Map<String, Long> noLeidasCount(@AuthenticationPrincipal UsuarioPrincipal principal) {
		long count = notificacionService.contarNoLeidas(principal.getId());
		return Map.of("count", count);
	}

	@PutMapping("/{id}/leer")
	public ResponseEntity<Notificacion> marcarLeida(@PathVariable String id,
			@AuthenticationPrincipal UsuarioPrincipal principal) {
		try {
			return ResponseEntity.ok(notificacionService.marcarLeida(id, principal.getId()));
		} catch (BusinessRuleException ex) {
			return ResponseEntity.status(403).build();
		}
	}
}
