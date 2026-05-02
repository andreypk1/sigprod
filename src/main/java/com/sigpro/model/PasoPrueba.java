package com.sigpro.model;

/**
 * Embebido en {@link CasoPrueba} (no es documento propio en MongoDB).
 */
public class PasoPrueba {

	private Integer orden;
	private String accion;
	private String resultadoEsperado;
	private String resultadoReal;
	private String resultado;

	public PasoPrueba() {
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getResultadoEsperado() {
		return resultadoEsperado;
	}

	public void setResultadoEsperado(String resultadoEsperado) {
		this.resultadoEsperado = resultadoEsperado;
	}

	public String getResultadoReal() {
		return resultadoReal;
	}

	public void setResultadoReal(String resultadoReal) {
		this.resultadoReal = resultadoReal;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
}
