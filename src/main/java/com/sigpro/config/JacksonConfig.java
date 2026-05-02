package com.sigpro.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tools.jackson.databind.json.JsonMapper;

/**
 * Spring Boot 4 / Jackson 3 expone {@link JsonMapper}, no el {@code ObjectMapper} clásico de Jackson 2.
 */
@Configuration
public class JacksonConfig {

	@Bean
	@ConditionalOnMissingBean(JsonMapper.class)
	JsonMapper jsonMapper() {
		return JsonMapper.builder().findAndAddModules().build();
	}
}
