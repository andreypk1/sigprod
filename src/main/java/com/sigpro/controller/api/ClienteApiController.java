package com.sigpro.controller.api;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sigpro.model.Cliente;
import com.sigpro.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
@PreAuthorize("isAuthenticated()")
public class ClienteApiController {

	private final ClienteService clienteService;

	public ClienteApiController(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	@GetMapping
	public List<Cliente> listar() {
		return clienteService.listar();
	}
}
