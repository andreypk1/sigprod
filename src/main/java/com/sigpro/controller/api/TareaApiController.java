package com.sigpro.controller.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sigpro.dto.TareaApiDto;
import com.sigpro.model.Tarea;
import com.sigpro.service.BusinessRuleException;
import com.sigpro.service.TareaService;

@RestController
@RequestMapping("/api/tareas")
@PreAuthorize("isAuthenticated()")
public class TareaApiController {

	private final TareaService tareaService;

	public TareaApiController(TareaService tareaService) {
		this.tareaService = tareaService;
	}

	@PostMapping
	public ResponseEntity<?> crear(@RequestBody TareaApiDto dto) {
		try {
			Tarea t = tareaService.crear(dto);
			return ResponseEntity.ok(t);
		} catch (BusinessRuleException ex) {
			return ResponseEntity.badRequest().body(Map.of("mensaje", ex.getMessage()));
		}
	}

	@PutMapping("/{id}/estado")
	public ResponseEntity<?> actualizarEstado(@PathVariable String id, @RequestBody Map<String, String> body) {
		String estado = body.get("estado");
		if (estado == null || estado.isBlank()) {
			return ResponseEntity.badRequest().body(Map.of("mensaje", "Estado requerido"));
		}
		try {
			Tarea t = tareaService.actualizarEstado(id, estado);
			return ResponseEntity.ok(t);
		} catch (BusinessRuleException ex) {
			return ResponseEntity.badRequest().body(Map.of("mensaje", ex.getMessage()));
		}
	}
}
