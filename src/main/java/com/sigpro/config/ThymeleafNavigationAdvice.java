package com.sigpro.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Thymeleaf 3.1+ ya no expone {@code #request} / {@code #httpServletRequest} en expresiones por defecto.
 * Se inyecta el URI actual en el modelo para resaltar el ítem activo del menú sin usar utilidades web retiradas.
 */
@ControllerAdvice
public class ThymeleafNavigationAdvice {

	@ModelAttribute("navRequestUri")
	public String navRequestUri(HttpServletRequest request) {
		return request.getRequestURI();
	}
}
