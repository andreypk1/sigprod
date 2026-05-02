package com.sigpro.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Ayuda a detectar por qué el driver usa localhost:27017 en lugar de Atlas (URI vacía o sobrescrita por env).
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MongoStartupDiagnostics implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(MongoStartupDiagnostics.class);

	private final Environment environment;

	public MongoStartupDiagnostics(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void run(ApplicationArguments args) {
		String uri = environment.getProperty("spring.data.mongodb.uri");
		String envUri = environment.getProperty("SPRING_DATA_MONGODB_URI");
		if (envUri != null && !envUri.isBlank()) {
			log.warn(
					"SPRING_DATA_MONGODB_URI está definida en el entorno y puede sobrescribir application.properties: {}",
					resumenUri(envUri));
		}
		String database = environment.getProperty("spring.data.mongodb.database");
		if (uri == null || uri.isBlank()) {
			log.error(
					"spring.data.mongodb.uri está vacía: Spring Boot usará localhost:27017 por defecto. Define la URI en application.properties o borra variables que la vacíen.");
		} else if (uri.contains("localhost") || uri.contains("127.0.0.1")) {
			log.warn("MongoDB apunta a local ({})", resumenUri(uri));
		} else {
			log.info("MongoDB URI configurada (sin credenciales): {}", resumenUri(uri));
		}
		log.info("MongoDB database (spring.data.mongodb.database): {}", database != null && !database.isBlank() ? database : "(inferida de la URI)");
	}

	private static String resumenUri(String uri) {
		if (uri == null) {
			return "(null)";
		}
		int scheme = uri.indexOf("://");
		int at = uri.indexOf('@');
		if (scheme >= 0 && at > scheme + 3) {
			return uri.substring(0, scheme + 3) + "***@" + uri.substring(at + 1);
		}
		return uri.length() > 80 ? uri.substring(0, 80) + "…" : uri;
	}
}
