package com.sigpro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sigpro.model.Cliente;
import com.sigpro.model.Proyecto;
import com.sigpro.repository.ClienteRepository;
import com.sigpro.repository.ProyectoRepository;

@Service
public class ClienteService {

	private final ClienteRepository clienteRepository;
	private final ProyectoRepository proyectoRepository;

	public ClienteService(ClienteRepository clienteRepository, ProyectoRepository proyectoRepository) {
		this.clienteRepository = clienteRepository;
		this.proyectoRepository = proyectoRepository;
	}

	public List<Cliente> listar() {
		return clienteRepository.findAll();
	}

	public Optional<Cliente> findById(String id) {
		return clienteRepository.findById(id);
	}

	public Cliente guardar(Cliente c) {
		if (c.getId() != null && c.getId().isBlank()) {
			c.setId(null);
		}
		Cliente saved = clienteRepository.save(c);
		String rs = saved.getRazonSocial();
		if (saved.getId() != null && rs != null) {
			for (Proyecto p : proyectoRepository.findByClienteId(saved.getId())) {
				if (!rs.equals(p.getClienteNombre())) {
					p.setClienteNombre(rs);
					proyectoRepository.save(p);
				}
			}
		}
		return saved;
	}

	public void eliminar(String id) {
		clienteRepository.deleteById(id);
	}
}
