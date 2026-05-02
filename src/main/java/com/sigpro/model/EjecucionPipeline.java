package com.sigpro.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "ejecuciones_pipeline")
public class EjecucionPipeline {

	@Id
	private String id;
	private String pipelineId;
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaFin;
	private String estado;
	private String commitHash;
	private String logsUrl;
	private String ejecutorId;

	public EjecucionPipeline() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPipelineId() {
		return pipelineId;
	}

	public void setPipelineId(String pipelineId) {
		this.pipelineId = pipelineId;
	}

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDateTime getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCommitHash() {
		return commitHash;
	}

	public void setCommitHash(String commitHash) {
		this.commitHash = commitHash;
	}

	public String getLogsUrl() {
		return logsUrl;
	}

	public void setLogsUrl(String logsUrl) {
		this.logsUrl = logsUrl;
	}

	public String getEjecutorId() {
		return ejecutorId;
	}

	public void setEjecutorId(String ejecutorId) {
		this.ejecutorId = ejecutorId;
	}
}
