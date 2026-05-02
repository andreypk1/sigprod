package com.sigpro.config;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private static final Logger log = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		String uri = request.getRequestURI();
		String ctx = request.getContextPath();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		log.warn("Acceso denegado: uri={} usuario={} mensaje={}", uri,
				auth != null ? auth.getName() : "(anon)",
				accessDeniedException.getMessage());
		if (uri.startsWith(ctx + "/api")) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write("{\"mensaje\":\"Acceso denegado\",\"codigo\":\"403\"}");
			return;
		}
		response.sendRedirect(ctx + "/acceso-denegado");
	}
}
