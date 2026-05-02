package com.sigpro.util;

public final class EstadosTarea {

	public static final String TODO = "TODO";
	public static final String EN_PROGRESO = "EN_PROGRESO";
	public static final String EN_REVISION = "EN_REVISION";
	public static final String EN_PRUEBAS = "EN_PRUEBAS";
	public static final String COMPLETADA = "COMPLETADA";
	public static final String BLOQUEADA = "BLOQUEADA";

	private EstadosTarea() {
	}

	public static String[] columnasKanban() {
		return new String[] { TODO, EN_PROGRESO, EN_REVISION, EN_PRUEBAS, COMPLETADA, BLOQUEADA };
	}
}
