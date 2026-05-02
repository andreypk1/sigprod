package com.sigpro.config;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * MongoDB no crea bases vacías: la base {@code sigprod} existe en el cluster cuando ocurre la primera escritura.
 * Este componente ejecuta un upsert idempotente al iniciar la aplicación para materializar la base (y la colección
 * {@code application_meta}) en cuanto las credenciales y la red permiten conectar a Atlas.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class MongoDatabaseBootstrap implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(MongoDatabaseBootstrap.class);

	static final String META_COLLECTION = "application_meta";
	static final String META_ID = "sigprod-bootstrap";

	private final MongoTemplate mongoTemplate;

	public MongoDatabaseBootstrap(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public void run(ApplicationArguments args) {
		try {
			Query q = Query.query(Criteria.where("_id").is(META_ID));
			Update u = new Update().set("application", "SIGPROD").set("lastStartedAt", Instant.now()).setOnInsert("createdAt",
					Instant.now());
			mongoTemplate.upsert(q, u, META_COLLECTION);
			log.info("MongoDB: base '{}' disponible (colección '{}' con documento de arranque).",
					mongoTemplate.getDb().getName(), META_COLLECTION);
		} catch (RuntimeException ex) {
			log.error(
					"No se pudo inicializar la base MongoDB al arrancar (Atlas → Network Access / URI / usuario DB). Sin esta escritura no existirá la base sigprod en Compass. Detalle:",
					ex);
		}
	}
}
