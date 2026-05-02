package com.sigpro.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Pantallas de marcador de posición para módulos aún sin pantalla propia: navegación verificable
 * y mismo modelo de seguridad que el resto (usuario autenticado).
 */
@Controller
@RequestMapping("/modulo")
public class ModuloPendienteController {

	private static final Map<String, String> TITULOS = new HashMap<>();

	static {
		TITULOS.put("riesgos", "Gestión de riesgos");
		TITULOS.put("presupuesto", "Presupuesto y costes");
		TITULOS.put("reportes", "Reportes e indicadores");
		TITULOS.put("ceremonias-agiles", "Ceremonias ágiles");
		TITULOS.put("impedimentos", "Impedimentos");
		TITULOS.put("priorizacion-moscow", "Priorización MoSCoW");
		TITULOS.put("solicitudes-cambio", "Solicitudes de cambio");
		TITULOS.put("srs", "Especificación de requisitos (SRS)");
		TITULOS.put("casos-uso", "Casos de uso");
		TITULOS.put("diagramas-bpmn", "Diagramas BPMN");
		TITULOS.put("matriz-trazabilidad", "Matriz de trazabilidad");
		TITULOS.put("glosario", "Glosario");
		TITULOS.put("entrevistas", "Entrevistas y hallazgos");
		TITULOS.put("modelo-dominio", "Modelo de dominio");
		TITULOS.put("adr", "Architecture Decision Records (ADR)");
		TITULOS.put("vista-despliegue", "Vista de despliegue");
		TITULOS.put("integraciones", "Mapa de integraciones");
		TITULOS.put("design-system", "Design system");
		TITULOS.put("flujos-usuario", "Flujos de usuario");
		TITULOS.put("accesibilidad", "Accesibilidad (a11y)");
		TITULOS.put("componentes-ui", "Biblioteca de componentes UI");
		TITULOS.put("storybook", "Storybook / showcase");
		TITULOS.put("contratos-api", "Contratos de API");
		TITULOS.put("esquema-bd", "Esquema de base de datos");
		TITULOS.put("automatizacion-qa", "Automatización de pruebas");
		TITULOS.put("metricas-calidad", "Métricas de calidad");
		TITULOS.put("observabilidad", "Observabilidad y alertas");
		TITULOS.put("infra-as-code", "Infraestructura como código");
		TITULOS.put("general", "Módulo");
	}

	@GetMapping("/pendiente")
	public String pendienteSinCodigo() {
		return "redirect:/modulo/pendiente/general";
	}

	/**
	 * Rutas con path segment ({@code /modulo/pendiente/riesgos}) para que Thymeleaf genere {@code href}
	 * estable (la forma {@code @{/modulo/pendiente(codigo='x')}} puede resolverse mal y dejar el enlace como {@code #}).
	 */
	@GetMapping("/pendiente/{codigo}")
	public String pendiente(@PathVariable String codigo, Model model) {
		String safe = codigo == null ? "general" : codigo.replaceAll("[^a-zA-Z0-9\\-]", "");
		if (safe.isEmpty()) {
			safe = "general";
		}
		model.addAttribute("tituloModulo", TITULOS.getOrDefault(safe, formatearCodigoLegible(safe)));
		model.addAttribute("codigoModulo", safe);
		return "modulos/pendiente";
	}

	private static String formatearCodigoLegible(String codigo) {
		return Stream.of(codigo.split("-"))
				.filter(s -> !s.isEmpty())
				.map(s -> s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1).toLowerCase(Locale.ROOT))
				.collect(Collectors.joining(" "));
	}
}
