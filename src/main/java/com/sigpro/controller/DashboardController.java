package com.sigpro.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sigpro.model.Proyecto;
import com.sigpro.model.RolSistema;
import com.sigpro.model.Usuario;
import com.sigpro.security.UsuarioPrincipal;
import com.sigpro.service.AdminResumenService;
import com.sigpro.service.DashboardService;
import com.sigpro.service.NotificacionService;

@Controller
public class DashboardController {

	private final DashboardService dashboardService;
	private final NotificacionService notificacionService;
	private final com.sigpro.service.ProyectoService proyectoService;
	private final AdminResumenService adminResumenService;

	public DashboardController(DashboardService dashboardService, NotificacionService notificacionService,
			com.sigpro.service.ProyectoService proyectoService, AdminResumenService adminResumenService) {
		this.dashboardService = dashboardService;
		this.notificacionService = notificacionService;
		this.proyectoService = proyectoService;
		this.adminResumenService = adminResumenService;
	}

	@GetMapping("/dashboard")
	public String dashboard(@AuthenticationPrincipal UsuarioPrincipal principal, Model model) {
		Usuario u = principal.getUsuario();
		model.addAttribute("usuario", u);
		model.addAttribute("rolMostrado", principal.getRolEfectivo());
		model.addAttribute("kpis", dashboardService.kpisPorRol(u));
		model.addAttribute("notificaciones", notificacionService.ultimasNoLeidas(u.getId()));
		model.addAttribute("notificacionesNoLeidas", notificacionService.contarNoLeidas(u.getId()));

		List<Proyecto> proyectos = proyectoService.listarFiltrado(null, null, null);
		if (u.getRol() != RolSistema.ADMINISTRADOR) {
			proyectos = proyectos.stream().filter(p -> usuarioEnProyecto(u.getId(), p)).toList();
		}
		model.addAttribute("proyectosAsignados", proyectos);

		if (u.getRol() == RolSistema.ADMINISTRADOR) {
			model.addAttribute("adminConteos", adminResumenService.conteosGlobales());
			model.addAttribute("adminHistoriasPorEstado", adminResumenService.historiasPorEstado());
		}

		return "dashboard/index";
	}

	private boolean usuarioEnProyecto(String uid, Proyecto p) {
		if (uid.equals(p.getProductOwnerId()) || uid.equals(p.getProjectManagerId())) {
			return true;
		}
		return p.getMiembros() != null && p.getMiembros().stream().anyMatch(m -> uid.equals(m.getUsuarioId()));
	}
}
