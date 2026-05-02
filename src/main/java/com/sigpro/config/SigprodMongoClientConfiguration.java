package com.sigpro.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

/**
 * Fuerza el uso de {@code spring.data.mongodb.uri} tal como la resuelve el {@link Environment}, evitando el fallo
 * observado donde el autoconfig creaba un cluster en {@code localhost:27017} pese a tener Atlas en
 * {@code application.properties} (p. ej. variables de entorno vacías, orden de carga o DevTools).
 */
@Configuration
public class SigprodMongoClientConfiguration {

	private static final Logger log = LoggerFactory.getLogger(SigprodMongoClientConfiguration.class);

	@Bean
	public MongoClient mongoClient(Environment environment) {
		String uri = environment.getProperty("spring.data.mongodb.uri");
		if (uri == null || uri.isBlank()) {
			throw new IllegalStateException(
					"spring.data.mongodb.uri está vacía. Revisa que no exista SPRING_DATA_MONGODB_URI vacía en el sistema/IDE, ni -Dspring.data.mongodb.uri= sin valor; define la URI de Atlas en application.properties.");
		}
		if (uri.contains("localhost") || uri.contains("127.0.0.1")) {
			log.warn("La URI de Mongo resuelta apunta a localhost. Si usas Atlas, elimina la variable que la pisa.");
		}
		log.info("MongoClient creado explícitamente con la URI resuelta por Spring: {}", resumenUri(uri));
		return MongoClients.create(uri.trim());
	}

	private static String resumenUri(String uri) {
		int scheme = uri.indexOf("://");
		int at = uri.indexOf('@');
		if (scheme >= 0 && at > scheme + 3) {
			return uri.substring(0, scheme + 3) + "***@" + uri.substring(at + 1);
		}
		return uri.length() > 80 ? uri.substring(0, 80) + "…" : uri;
	}
}
