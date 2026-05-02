package com.sigpro.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Component;

import com.mongodb.MongoException;

@Component
@Profile("dev")
@Order
public class DevDataSeeder implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(DevDataSeeder.class);

	private final ReferenceProjectsDataSeed referenceProjectsDataSeed;
	private final String mongoDatabase;

	public DevDataSeeder(ReferenceProjectsDataSeed referenceProjectsDataSeed,
			@Value("${spring.data.mongodb.database:sigprod}") String mongoDatabase) {
		this.referenceProjectsDataSeed = referenceProjectsDataSeed;
		this.mongoDatabase = mongoDatabase;
	}

	@Override
	public void run(String... args) {
		try {
			referenceProjectsDataSeed.seedIfAbsent();
			log.info("DevDataSeeder: datos de referencia aplicados (usuarios/proyectos idempotentes). Base '{}'.",
					mongoDatabase);
		} catch (DataAccessResourceFailureException | MongoException ex) {
			log.warn(
					"MongoDB no disponible o timeout; se omite DevDataSeeder. Comprueba URI Atlas y red. Causa: {}",
					ex.getMessage());
		} catch (RuntimeException ex) {
			log.warn("No se pudo completar DevDataSeeder: {}", ex.toString());
		}
	}
}
