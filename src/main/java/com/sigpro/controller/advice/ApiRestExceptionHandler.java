package com.sigpro.controller.advice;

import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sigpro.dto.ApiError;
import com.sigpro.service.BusinessRuleException;

@RestControllerAdvice(basePackages = "com.sigpro.controller.api")
public class ApiRestExceptionHandler {

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ApiError> noEncontrado(NoSuchElementException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ApiError("Recurso no encontrado", "404"));
	}

	@ExceptionHandler(BusinessRuleException.class)
	public ResponseEntity<ApiError> negocio(BusinessRuleException ex) {
		return ResponseEntity.badRequest().body(new ApiError(ex.getMessage(), "400"));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiError> denegado() {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiError("Acceso denegado", "403"));
	}
}
