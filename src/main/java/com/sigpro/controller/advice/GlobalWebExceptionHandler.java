package com.sigpro.controller.advice;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.sigpro.service.BusinessRuleException;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalWebExceptionHandler {

	@ExceptionHandler(NoSuchElementException.class)
	public ModelAndView noEncontrado(HttpServletResponse response) {
		response.setStatus(HttpStatus.NOT_FOUND.value());
		return new ModelAndView("error/404");
	}

	@ExceptionHandler(BusinessRuleException.class)
	public ModelAndView reglas(BusinessRuleException ex) {
		ModelAndView mav = new ModelAndView("error/500");
		mav.addObject("mensaje", ex.getMessage());
		return mav;
	}
}
