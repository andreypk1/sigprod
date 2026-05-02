package com.sigpro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sigpro.security.SigprodUserDetailsService;
import com.sigpro.security.UsuarioPrincipal;
import com.sigpro.service.AuditoriaService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private final SigprodUserDetailsService userDetailsService;
	private final AuditoriaService auditoriaService;
	private final CustomAccessDeniedHandler accessDeniedHandler;

	public SecurityConfig(SigprodUserDetailsService userDetailsService, AuditoriaService auditoriaService,
			CustomAccessDeniedHandler accessDeniedHandler) {
		this.userDetailsService = userDetailsService;
		this.auditoriaService = auditoriaService;
		this.accessDeniedHandler = accessDeniedHandler;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	/**
	 * ADMINISTRADOR tiene acceso total: rutas {@code /admin/**}, {@code /auditoria/**} y está incluido en cada
	 * regla por módulo (proyectos, backlog, sprints, diseño, qa, etc.) para operar todo el sistema, no solo la consola de admin.
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
			AuthenticationSuccessHandler loginSuccessHandler,
			AuthenticationFailureHandler loginFailureHandler,
			LogoutSuccessHandler logoutSuccessHandler
	) throws Exception {
		http
				.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
				.userDetailsService(userDetailsService)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/", "/login", "/registro", "/css/**", "/js/**", "/images/**", "/webjars/**",
								"/error", "/acceso-denegado").permitAll()
						.requestMatchers("/dashboard").authenticated()
						.requestMatchers("/analista/**").hasAnyRole("ADMINISTRADOR", "ANALISTA_NEGOCIO", "PROJECT_MANAGER")
						.requestMatchers("/area/arquitectura/**").hasAnyRole("ARQUITECTO_SOFTWARE", "ADMINISTRADOR")
						.requestMatchers("/arquitectura/**").hasAnyRole("ARQUITECTO_SOFTWARE", "ADMINISTRADOR")
						.requestMatchers("/area/devops/**").hasAnyRole("DEVOPS_ENGINEER", "ADMINISTRADOR")
						.requestMatchers("/devops/**").hasAnyRole("DEVOPS_ENGINEER", "ADMINISTRADOR")
						.requestMatchers("/perfil/**").authenticated()
						.requestMatchers("/exportar/**").authenticated()
						.requestMatchers("/notificaciones/**").authenticated()
						.requestMatchers("/proyectos/**").hasAnyRole("ADMINISTRADOR", "PROJECT_MANAGER", "PRODUCT_OWNER")
						.requestMatchers("/backlog/**").hasAnyRole("ADMINISTRADOR", "PRODUCT_OWNER", "PROJECT_MANAGER",
								"ANALISTA_NEGOCIO")
						.requestMatchers("/sprints/**").hasAnyRole("ADMINISTRADOR", "PROJECT_MANAGER", "PRODUCT_OWNER",
								"DESARROLLADOR_FRONTEND", "DESARROLLADOR_BACKEND")
						.requestMatchers("/tareas/**").authenticated()
						.requestMatchers("/kanban/**").authenticated()
						.requestMatchers("/diseno/**").hasAnyRole("ADMINISTRADOR", "DISENADOR_UI_UX",
								"DESARROLLADOR_FRONTEND")
						.requestMatchers("/qa/**").hasAnyRole("ADMINISTRADOR", "QA_ENGINEER", "PROJECT_MANAGER")
						.requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
						.requestMatchers("/auditoria/**").hasRole("ADMINISTRADOR")
						.requestMatchers("/api/**").authenticated()
						.anyRequest().authenticated())
				.formLogin(form -> form
						.loginPage("/login")
						.loginProcessingUrl("/login")
						.usernameParameter("correo")
						.passwordParameter("password")
						.successHandler(loginSuccessHandler)
						.failureHandler(loginFailureHandler)
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessHandler(logoutSuccessHandler)
						.permitAll())
				.exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler));

		return http.build();
	}

	@Bean
	public AuthenticationSuccessHandler loginSuccessHandler() {
		return (request, response, authentication) -> {
			UsuarioPrincipal up = (UsuarioPrincipal) authentication.getPrincipal();
			try {
				auditoriaService.registrar(up.getId(), up.nombreCompleto(), "LOGIN", "SESION", up.getId(), null, null,
						request.getRemoteAddr());
			} catch (RuntimeException ex) {
				// No tumbar el login si Mongo falla al escribir auditoría
			}
			response.sendRedirect(request.getContextPath() + "/dashboard");
		};
	}

	@Bean
	public AuthenticationFailureHandler loginFailureHandler() {
		return (request, response, exception) -> response
				.sendRedirect(request.getContextPath() + "/login?badCredentials");
	}

	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return (request, response, authentication) -> {
			if (authentication != null && authentication.getPrincipal() instanceof UsuarioPrincipal) {
				UsuarioPrincipal up = (UsuarioPrincipal) authentication.getPrincipal();
				try {
					auditoriaService.registrar(up.getId(), up.nombreCompleto(), "LOGOUT", "SESION", up.getId(), null, null,
							request.getRemoteAddr());
				} catch (RuntimeException ex) {
					// No tumbar el logout si Mongo falla al escribir auditoría
				}
			}
			String home = ServletUriComponentsBuilder.fromContextPath(request).path("/").build().toUriString();
			response.sendRedirect(response.encodeRedirectURL(home));
		};
	}
}
