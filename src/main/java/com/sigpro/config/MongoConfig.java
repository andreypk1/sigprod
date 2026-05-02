package com.sigpro.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Habilita auditoría Mongo. La conexión usa {@link SigprodMongoClientConfiguration} con
 * {@code spring.data.mongodb.uri}; la base {@code sigprod} viene de {@code spring.data.mongodb.database}.
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig {
}
