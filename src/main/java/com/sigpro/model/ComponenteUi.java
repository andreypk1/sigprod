package com.sigpro.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "componentes_ui")
public class ComponenteUi {

	@Id
	private String id;
	private String designSystemId;
	private String proyectoId;
	private String nombre;
	private String categoria;
	private String codigo;
	private String propiedades;
	private String version;

	public ComponenteUi() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDesignSystemId() {
		return designSystemId;
	}

	public void setDesignSystemId(String designSystemId) {
		this.designSystemId = designSystemId;
	}

	public String getProyectoId() {
		return proyectoId;
	}

	public void setProyectoId(String proyectoId) {
		this.proyectoId = proyectoId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getPropiedades() {
		return propiedades;
	}

	public void setPropiedades(String propiedades) {
		this.propiedades = propiedades;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
