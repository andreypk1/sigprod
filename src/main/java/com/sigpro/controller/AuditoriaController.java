package com.sigpro.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sigpro.model.BitacoraAuditoria;
import com.sigpro.repository.BitacoraAuditoriaRepository;

@Controller
@RequestMapping("/auditoria")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AuditoriaController {

	private final BitacoraAuditoriaRepository bitacoraAuditoriaRepository;

	public AuditoriaController(BitacoraAuditoriaRepository bitacoraAuditoriaRepository) {
		this.bitacoraAuditoriaRepository = bitacoraAuditoriaRepository;
	}

	@GetMapping
	public String index(@RequestParam(required = false) String usuarioId,
			@RequestParam(required = false) String accion, @RequestParam(required = false) String entidad,
			@RequestParam(required = false) LocalDate desde, @RequestParam(required = false) LocalDate hasta,
			Model model) {

		List<BitacoraAuditoria> todos = bitacoraAuditoriaRepository.findAll();
		LocalDateTime desdeDt = desde != null ? desde.atStartOfDay() : null;
		LocalDateTime hastaDt = hasta != null ? hasta.atTime(LocalTime.MAX) : null;

		List<BitacoraAuditoria> filtrados = todos.stream().filter(b -> usuarioId == null || usuarioId.isBlank()
				|| usuarioId.equals(b.getUsuarioId())).filter(b -> accion == null || accion.isBlank()
						|| accion.equalsIgnoreCase(b.getAccion()))
				.filter(b -> entidad == null || entidad.isBlank() || entidad.equalsIgnoreCase(b.getEntidad()))
				.filter(b -> desdeDt == null || (b.getFecha() != null && !b.getFecha().isBefore(desdeDt)))
				.filter(b -> hastaDt == null || (b.getFecha() != null && !b.getFecha().isAfter(hastaDt)))
				.collect(Collectors.toList());

		model.addAttribute("registros", filtrados);
		model.addAttribute("filtroUsuario", usuarioId);
		model.addAttribute("filtroAccion", accion);
		model.addAttribute("filtroEntidad", entidad);
		model.addAttribute("filtroDesde", desde);
		model.addAttribute("filtroHasta", hasta);
		return "admin/auditoria";
	}
}
