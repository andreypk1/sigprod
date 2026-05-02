package com.sigpro.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import tools.jackson.databind.json.JsonMapper;

import com.sigpro.model.BitacoraAuditoria;
import com.sigpro.repository.BitacoraAuditoriaRepository;

@Service
public class AuditoriaService {

	private final BitacoraAuditoriaRepository bitacoraAuditoriaRepository;
	private final JsonMapper jsonMapper;

	public AuditoriaService(BitacoraAuditoriaRepository bitacoraAuditoriaRepository, JsonMapper jsonMapper) {
		this.bitacoraAuditoriaRepository = bitacoraAuditoriaRepository;
		this.jsonMapper = jsonMapper;
	}

	public void registrar(String usuarioId, String usuarioNombre, String accion, String entidad, String entidadId,
			Object datosAnteriores, Object datosNuevos, String ip) {
		BitacoraAuditoria b = new BitacoraAuditoria();
		b.setUsuarioId(usuarioId);
		b.setUsuarioNombre(usuarioNombre);
		b.setAccion(accion);
		b.setEntidad(entidad);
		b.setEntidadId(entidadId);
		try {
			if (datosAnteriores != null) {
				b.setDatosAnteriores(jsonMapper.writeValueAsString(datosAnteriores));
			}
			if (datosNuevos != null) {
				b.setDatosNuevos(jsonMapper.writeValueAsString(datosNuevos));
			}
		} catch (Exception ignored) {
			b.setDatosAnteriores(String.valueOf(datosAnteriores));
			b.setDatosNuevos(String.valueOf(datosNuevos));
		}
		b.setFecha(LocalDateTime.now());
		b.setIp(ip);
		bitacoraAuditoriaRepository.save(b);
	}
}
