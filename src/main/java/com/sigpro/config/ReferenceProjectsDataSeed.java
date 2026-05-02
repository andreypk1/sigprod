package com.sigpro.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sigpro.model.Cliente;
import com.sigpro.model.CriterioAceptacion;
import com.sigpro.model.Defecto;
import com.sigpro.model.Epica;
import com.sigpro.model.HistoriaUsuario;
import com.sigpro.model.MiembroProyecto;
import com.sigpro.model.Proyecto;
import com.sigpro.model.Riesgo;
import com.sigpro.model.RolSistema;
import com.sigpro.model.Sprint;
import com.sigpro.model.Tarea;
import com.sigpro.model.Usuario;
import com.sigpro.repository.ClienteRepository;
import com.sigpro.repository.DefectoRepository;
import com.sigpro.repository.EpicaRepository;
import com.sigpro.repository.HistoriaUsuarioRepository;
import com.sigpro.repository.ProyectoRepository;
import com.sigpro.repository.RiesgoRepository;
import com.sigpro.repository.SprintRepository;
import com.sigpro.repository.TareaRepository;
import com.sigpro.repository.UsuarioRepository;
import com.sigpro.util.EstadosTarea;

@Component
@Profile("dev")
public class ReferenceProjectsDataSeed {

	private static final Logger log = LoggerFactory.getLogger(ReferenceProjectsDataSeed.class);

	private final UsuarioRepository usuarioRepository;
	private final ClienteRepository clienteRepository;
	private final ProyectoRepository proyectoRepository;
	private final EpicaRepository epicaRepository;
	private final HistoriaUsuarioRepository historiaUsuarioRepository;
	private final SprintRepository sprintRepository;
	private final TareaRepository tareaRepository;
	private final DefectoRepository defectoRepository;
	private final RiesgoRepository riesgoRepository;
	private final PasswordEncoder passwordEncoder;

	public ReferenceProjectsDataSeed(UsuarioRepository usuarioRepository, ClienteRepository clienteRepository,
			ProyectoRepository proyectoRepository, EpicaRepository epicaRepository,
			HistoriaUsuarioRepository historiaUsuarioRepository, SprintRepository sprintRepository,
			TareaRepository tareaRepository, DefectoRepository defectoRepository,
			RiesgoRepository riesgoRepository, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.clienteRepository = clienteRepository;
		this.proyectoRepository = proyectoRepository;
		this.epicaRepository = epicaRepository;
		this.historiaUsuarioRepository = historiaUsuarioRepository;
		this.sprintRepository = sprintRepository;
		this.tareaRepository = tareaRepository;
		this.defectoRepository = defectoRepository;
		this.riesgoRepository = riesgoRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public void seedIfAbsent() {
		ensureUsers();
		Map<String, Usuario> u = loadUsersByEmail();
		Cliente bc = ensureCliente("Bancolombia S.A.", "890.903.938-8", "Jorge Restrepo",
				"j.restrepo@bancolombia.com.co", "+57 4 4042276", "Medellín");
		Cliente hc = ensureCliente("Clínica Foscal Internacional", "804.009.282-1", "Ana Suárez",
				"a.suarez@foscal.com.co", "+57 7 6384400", "Bucaramanga");
		Cliente ak = ensureCliente("Alkosto S.A.S.", "860.034.313-4", "Ricardo Molina",
				"r.molina@alkosto.com", "+57 1 7456700", "Bogotá");

		if (!proyectoRepository.existsByNombreIgnoreCase("Portal de Banca Digital")) {
			seedBanca(u, bc);
		}
		if (!proyectoRepository.existsByNombreIgnoreCase("Sistema de Historia Clínica Electrónica")) {
			seedHistoriaClinica(u, hc);
		}
		if (!proyectoRepository.existsByNombreIgnoreCase("Plataforma E-commerce Alkosto Digital")) {
			seedAlkosto(u, ak);
		}
	}

	private Map<String, Usuario> loadUsersByEmail() {
		Map<String, Usuario> m = new LinkedHashMap<>();
		for (Usuario x : usuarioRepository.findAll()) {
			m.put(x.getCorreo().toLowerCase(), x);
		}
		return m;
	}

	private void ensureUsers() {
		createUserIfAbsent("Admin", "Sistema", "admin@sigprod.com", RolSistema.ADMINISTRADOR);
		createUserIfAbsent("Valentina", "Rueda Ortiz", "v.rueda@sigprod.com", RolSistema.PRODUCT_OWNER);
		createUserIfAbsent("Andrés", "Cárdenas Mejía", "a.cardenas@sigprod.com", RolSistema.PRODUCT_OWNER);
		createUserIfAbsent("Laura", "Gómez Herrera", "l.gomez@sigprod.com", RolSistema.PROJECT_MANAGER);
		createUserIfAbsent("Carlos", "Pinto Vargas", "c.pinto@sigprod.com", RolSistema.PROJECT_MANAGER);
		createUserIfAbsent("Sofía", "Martínez Díaz", "s.martinez@sigprod.com", RolSistema.ANALISTA_NEGOCIO);
		createUserIfAbsent("Felipe", "Torres Lozano", "f.torres@sigprod.com", RolSistema.ARQUITECTO_SOFTWARE);
		createUserIfAbsent("Camila", "Vargas Peña", "c.vargas@sigprod.com", RolSistema.DISENADOR_UI_UX);
		createUserIfAbsent("Sebastián", "Mora Jiménez", "s.mora@sigprod.com", RolSistema.DESARROLLADOR_FRONTEND);
		createUserIfAbsent("Daniela", "Castro Rivas", "d.castro@sigprod.com", RolSistema.DESARROLLADOR_FRONTEND);
		createUserIfAbsent("Mateo", "Álvarez Quintero", "m.alvarez@sigprod.com", RolSistema.DESARROLLADOR_BACKEND);
		createUserIfAbsent("Juliana", "Sánchez Forero", "j.sanchez@sigprod.com", RolSistema.DESARROLLADOR_BACKEND);
		createUserIfAbsent("David", "Ramírez Ospina", "d.ramirez@sigprod.com", RolSistema.QA_ENGINEER);
		createUserIfAbsent("Isabella", "Niño Guerrero", "i.nino@sigprod.com", RolSistema.DEVOPS_ENGINEER);
	}

	private void createUserIfAbsent(String n, String a, String email, RolSistema rol) {
		if (usuarioRepository.findByCorreoIgnoreCase(email).isPresent()) {
			return;
		}
		Usuario u = new Usuario();
		u.setNombres(n);
		u.setApellidos(a);
		u.setCorreo(email);
		u.setRol(rol);
		u.setEstado("ACTIVO");
		u.setTelefono("+57 300 0000000");
		String plain = "admin@sigprod.com".equalsIgnoreCase(email != null ? email.trim() : "") ? "admin"
				: "demo12345";
		u.setPasswordHash(passwordEncoder.encode(plain));
		usuarioRepository.save(u);
		log.info("Usuario demo creado: {}", email);
	}

	private Cliente ensureCliente(String rs, String nit, String contacto, String correo, String tel, String ciudad) {
		return clienteRepository.findByNitIgnoreCase(nit).orElseGet(() -> {
			Cliente c = new Cliente();
			c.setRazonSocial(rs);
			c.setNit(nit);
			c.setContactoPrincipal(contacto);
			c.setCorreo(correo);
			c.setTelefono(tel);
			c.setCiudad(ciudad);
			c.setPais("Colombia");
			return clienteRepository.save(c);
		});
	}

	private Usuario require(Map<String, Usuario> m, String email) {
		Usuario u = m.get(email.toLowerCase());
		if (u == null) {
			throw new IllegalStateException("Falta usuario en seed: " + email);
		}
		return u;
	}

	private MiembroProyecto mp(Usuario user, RolSistema rol, int dedicacion, double tarifa) {
		MiembroProyecto x = new MiembroProyecto();
		x.setUsuarioId(user.getId());
		x.setUsuarioNombre(user.nombreCompleto().trim());
		x.setCorreo(user.getCorreo());
		x.setRolEnProyecto(rol);
		x.setPorcentajeDedicacion(dedicacion);
		x.setTarifaHora(tarifa);
		x.setFechaAsignacion(LocalDate.of(2025, 2, 1));
		return x;
	}

	private CriterioAceptacion ca(String desc) {
		CriterioAceptacion c = new CriterioAceptacion();
		c.setId(UUID.randomUUID().toString());
		c.setTipo("GHERKIN");
		c.setDescripcion(desc);
		c.setOrden(1);
		return c;
	}

	private void seedBanca(Map<String, Usuario> u, Cliente cli) {
		Usuario vrueda = require(u, "v.rueda@sigprod.com");
		Usuario lgomez = require(u, "l.gomez@sigprod.com");
		Usuario smartinez = require(u, "s.martinez@sigprod.com");
		Usuario ftorres = require(u, "f.torres@sigprod.com");
		Usuario cvargas = require(u, "c.vargas@sigprod.com");
		Usuario smora = require(u, "s.mora@sigprod.com");
		Usuario malvarez = require(u, "m.alvarez@sigprod.com");
		Usuario dramirez = require(u, "d.ramirez@sigprod.com");
		Usuario inino = require(u, "i.nino@sigprod.com");

		Proyecto p = new Proyecto();
		p.setNombre("Portal de Banca Digital");
		p.setDescripcion("Portal omnicanal de banca digital para clientes retail y PYME.");
		p.setClienteId(cli.getId());
		p.setClienteNombre(cli.getRazonSocial());
		p.setProductOwnerId(vrueda.getId());
		p.setProductOwnerNombre(vrueda.nombreCompleto().trim());
		p.setProjectManagerId(lgomez.getId());
		p.setProjectManagerNombre(lgomez.nombreCompleto().trim());
		p.setMetodologia("SCRUM");
		p.setEstado("ACTIVO");
		p.setPresupuesto(280_000_000d);
		p.setFechaInicio(LocalDate.of(2025, 2, 3));
		p.setFechaFin(LocalDate.of(2025, 6, 20));
		p.setProgresoPorcentaje(95);
		p.setTotalPuntosProducto(195);
		p.setBurnupAcumulado(Arrays.asList(0d, 42d, 80d, 125d, 165d, 200d, 235d, 251d));
		p.setBurndownRestante(Arrays.asList(195d, 153d, 115d, 70d, 30d, -5d, -40d, -56d));
		p.setEtiquetasBurnSprints(Arrays.asList("Inicio", "Sprint 1", "Sprint 2", "Sprint 3", "Sprint 4", "Sprint 5",
				"Sprint 6", "Sprint 7"));
		p.getMiembros().add(mp(vrueda, RolSistema.PRODUCT_OWNER, 30, 85_000));
		p.getMiembros().add(mp(lgomez, RolSistema.PROJECT_MANAGER, 60, 95_000));
		p.getMiembros().add(mp(smartinez, RolSistema.ANALISTA_NEGOCIO, 80, 75_000));
		p.getMiembros().add(mp(ftorres, RolSistema.ARQUITECTO_SOFTWARE, 50, 110_000));
		p.getMiembros().add(mp(cvargas, RolSistema.DISENADOR_UI_UX, 100, 80_000));
		p.getMiembros().add(mp(smora, RolSistema.DESARROLLADOR_FRONTEND, 100, 90_000));
		p.getMiembros().add(mp(malvarez, RolSistema.DESARROLLADOR_BACKEND, 100, 90_000));
		p.getMiembros().add(mp(dramirez, RolSistema.QA_ENGINEER, 80, 75_000));
		p.getMiembros().add(mp(inino, RolSistema.DEVOPS_ENGINEER, 50, 100_000));
		p = proyectoRepository.save(p);
		String pid = p.getId();

		Epica e1 = saveEpica(pid, "Autenticación y Seguridad de Usuarios", "COMPLETADA");
		Epica e2 = saveEpica(pid, "Gestión de Cuentas y Saldos", "COMPLETADA");
		Epica e3 = saveEpica(pid, "Transferencias y Pagos", "COMPLETADA");
		Epica e4 = saveEpica(pid, "Notificaciones y Alertas", "COMPLETADA");
		Epica e5 = saveEpica(pid, "Panel Administrativo del Banco", "EN_REVISION");

		List<Sprint> sp = new ArrayList<>();
		sp.add(saveSprint(pid, 1, "Fundamentos y Autenticación", LocalDate.of(2025, 2, 3), LocalDate.of(2025, 2, 14), 160,
				"COMPLETADO"));
		sp.add(saveSprint(pid, 2, "Gestión de Cuentas", LocalDate.of(2025, 2, 17), LocalDate.of(2025, 2, 28), 160,
				"COMPLETADO"));
		sp.add(saveSprint(pid, 3, "Módulo de Transferencias", LocalDate.of(2025, 3, 3), LocalDate.of(2025, 3, 14), 160,
				"COMPLETADO"));
		sp.add(saveSprint(pid, 4, "Pagos de Servicios y PSE", LocalDate.of(2025, 3, 17), LocalDate.of(2025, 3, 28), 160,
				"COMPLETADO"));
		sp.add(saveSprint(pid, 5, "Notificaciones Push y Email", LocalDate.of(2025, 3, 31), LocalDate.of(2025, 4, 11),
				160, "COMPLETADO"));
		sp.add(saveSprint(pid, 6, "Panel Administrativo — parte 1", LocalDate.of(2025, 4, 14), LocalDate.of(2025, 4, 25),
				160, "COMPLETADO"));
		sp.add(saveSprint(pid, 7, "Panel Administrativo — cierre y estabilización", LocalDate.of(2025, 4, 28),
				LocalDate.of(2025, 5, 9), 160, "ACTIVO"));
		sprintRepository.saveAll(sp);

		String s1 = sp.get(0).getId();
		String s2 = sp.get(1).getId();
		String s3 = sp.get(2).getId();
		String s4 = sp.get(3).getId();
		String s5 = sp.get(4).getId();
		String s6 = sp.get(5).getId();
		String s7 = sp.get(6).getId();

		saveHu(pid, e1.getId(), "Login con doble factor de autenticación",
				"Como usuario del banco, quiero iniciar sesión con 2FA para proteger mi cuenta.", "MUST", 8, "ACEPTADA",
				s1, List.of(ca("GIVEN: El usuario tiene 2FA activado WHEN: Ingresa credenciales correctas THEN: OTP al celular")));
		saveHu(pid, e1.getId(), "Registro de nuevo usuario digital",
				"Como cliente nuevo, quiero registrarme en la app para acceder a mis cuentas.", "MUST", 13, "ACEPTADA",
				s1, List.of(ca("GIVEN: Cédula válida WHEN: Formulario THEN: Cuenta en <2 min")));
		saveHu(pid, e1.getId(), "Recuperación de contraseña por SMS", "Como usuario, quiero recuperar contraseña por SMS.",
				"MUST", 5, "ACEPTADA", s1, List.of());
		saveHu(pid, e1.getId(), "Bloqueo automático por intentos fallidos",
				"Como banco, quiero bloqueo tras 3 intentos fallidos.", "MUST", 3, "ACEPTADA", s1, List.of());

		saveHu(pid, e2.getId(), "Consulta de saldo en tiempo real", null, "MUST", 5, "ACEPTADA", s2, List.of());
		saveHu(pid, e2.getId(), "Historial de movimientos con filtros", null, "MUST", 8, "ACEPTADA", s2, List.of());
		saveHu(pid, e2.getId(), "Descarga de extracto en PDF", null, "SHOULD", 5, "ACEPTADA", s2, List.of());
		saveHu(pid, e2.getId(), "Vista multi-cuenta (ahorros, corriente, CDT)", null, "SHOULD", 8, "ACEPTADA", s2,
				List.of());

		saveHu(pid, e3.getId(), "Transferencia entre cuentas propias", null, "MUST", 8, "ACEPTADA", s3, List.of());
		saveHu(pid, e3.getId(), "Transferencia a terceros por número de cuenta", null, "MUST", 13, "ACEPTADA", s3,
				List.of());
		saveHu(pid, e3.getId(), "Transferencia PSE a terceros bancos", null, "MUST", 13, "ACEPTADA", s3, List.of());
		saveHu(pid, e3.getId(), "Programar transferencias recurrentes", null, "COULD", 8, "ACEPTADA", s3, List.of());

		saveHu(pid, e3.getId(), "Pago de servicios públicos (EPM, Surtigas, ESSA)", null, "MUST", 8, "ACEPTADA", s4,
				List.of());
		saveHu(pid, e3.getId(), "Pago de tarjetas de crédito", null, "MUST", 8, "ACEPTADA", s4, List.of());
		saveHu(pid, e3.getId(), "Recargas de celular", null, "SHOULD", 5, "ACEPTADA", s4, List.of());

		saveHu(pid, e4.getId(), "Alerta push por movimiento en cuenta", null, "MUST", 8, "ACEPTADA", s5, List.of());
		saveHu(pid, e4.getId(), "Configuración de alertas personalizadas", null, "SHOULD", 5, "ACEPTADA", s5, List.of());
		saveHu(pid, e4.getId(), "Notificación de intentos de acceso fallidos", null, "MUST", 3, "ACEPTADA", s5,
				List.of());

		saveHu(pid, e5.getId(), "CRUD de usuarios en panel administrativo", null, "MUST", 8, "ACEPTADA", s6, List.of());
		saveHu(pid, e5.getId(), "Reportes de transacciones por rango de fechas", null, "MUST", 8, "ACEPTADA", s6,
				List.of());
		saveHu(pid, e5.getId(), "Dashboard de métricas operacionales", null, "SHOULD", 5, "EN_REVISION", s7, List.of());
		saveHu(pid, e5.getId(), "Exportación de reportes a Excel y PDF", null, "SHOULD", 5, "EN_REVISION", s7, List.of());

		String huDashId = findHuByTitle(pid, "Dashboard de métricas operacionales").getId();
		String huExportId = findHuByTitle(pid, "Exportación de reportes a Excel y PDF").getId();
		saveTarea(pid, huDashId, s7, "Conectar endpoint de métricas al dashboard", EstadosTarea.EN_REVISION, "FRONTEND",
				smora.getId(), smora.nombreCompleto().trim(), "ALTA");
		saveTarea(pid, huExportId, s7, "Exportación PDF con iText", EstadosTarea.EN_REVISION, "BACKEND",
				malvarez.getId(), malvarez.nombreCompleto().trim(), "MEDIA");
		saveTarea(pid, huDashId, s7, "Implementar gráfica de transacciones diarias", EstadosTarea.COMPLETADA, "BACKEND",
				malvarez.getId(), malvarez.nombreCompleto().trim(), "ALTA");
		saveTarea(pid, huDashId, s7, "Diseñar componente de reportes", EstadosTarea.COMPLETADA, "FRONTEND",
				smora.getId(), smora.nombreCompleto().trim(), "MEDIA");
		saveTarea(pid, huExportId, s7, "Pruebas de regresión panel admin", EstadosTarea.EN_PROGRESO, "QA",
				dramirez.getId(), dramirez.nombreCompleto().trim(), "ALTA");
		saveTarea(pid, huExportId, s7, "Deploy a staging ambiente final", EstadosTarea.TODO, "DEVOPS", inino.getId(),
				inino.nombreCompleto().trim(), "CRITICA");

		saveRiesgo(pid, "Latencia en consultas de saldo en hora pico", 2, 4, "MITIGANDO", lgomez.getId());
		saveRiesgo(pid, "Cambio de API del banco central", 1, 5, "CERRADO", lgomez.getId());

		saveDefecto(pid, "Exportación PDF corta texto en columnas anchas", "MEDIA", "EN_DESARROLLO", dramirez.getId(),
				malvarez.getId());
		saveDefecto(pid, "Gráfica de barras no carga en Safari", "BAJA", "ASIGNADO", dramirez.getId(), smora.getId());

		log.info("Seed proyecto Banca Digital completado.");
	}

	private HistoriaUsuario findHuByTitle(String proyectoId, String title) {
		return historiaUsuarioRepository.findByProyectoId(proyectoId).stream()
				.filter(h -> title.equals(h.getTitulo())).findFirst().orElseThrow();
	}

	private void seedHistoriaClinica(Map<String, Usuario> u, Cliente cli) {
		Usuario acardenas = require(u, "a.cardenas@sigprod.com");
		Usuario cpinto = require(u, "c.pinto@sigprod.com");
		Usuario smartinez = require(u, "s.martinez@sigprod.com");
		Usuario ftorres = require(u, "f.torres@sigprod.com");
		Usuario cvargas = require(u, "c.vargas@sigprod.com");
		Usuario dcastro = require(u, "d.castro@sigprod.com");
		Usuario jsanchez = require(u, "j.sanchez@sigprod.com");
		Usuario dramirez = require(u, "d.ramirez@sigprod.com");
		Usuario inino = require(u, "i.nino@sigprod.com");

		Proyecto p = new Proyecto();
		p.setNombre("Sistema de Historia Clínica Electrónica");
		p.setDescripcion("HC electrónica conforme a normativa colombiana.");
		p.setClienteId(cli.getId());
		p.setClienteNombre(cli.getRazonSocial());
		p.setProductOwnerId(acardenas.getId());
		p.setProductOwnerNombre(acardenas.nombreCompleto().trim());
		p.setProjectManagerId(cpinto.getId());
		p.setProjectManagerNombre(cpinto.nombreCompleto().trim());
		p.setMetodologia("SCRUM");
		p.setEstado("ACTIVO");
		p.setPresupuesto(420_000_000d);
		p.setFechaInicio(LocalDate.of(2025, 3, 10));
		p.setFechaFin(LocalDate.of(2025, 10, 31));
		p.setProgresoPorcentaje(55);
		p.setTotalPuntosProducto(280);
		p.setBurnupAcumulado(Arrays.asList(0d, 44d, 84d, 122d, 157d, 179d, 179d, 179d, 179d));
		p.setBurndownRestante(
				Arrays.asList(280d, 236d, 196d, 158d, 123d, 101d, 101d, 101d, 101d));
		p.setEtiquetasBurnSprints(Arrays.asList("Inicio", "S1", "S2", "S3", "S4", "S5(parcial)", "S6", "S7", "S8"));
		p.getMiembros().add(mp(acardenas, RolSistema.PRODUCT_OWNER, 40, 85_000));
		p.getMiembros().add(mp(cpinto, RolSistema.PROJECT_MANAGER, 70, 95_000));
		p.getMiembros().add(mp(smartinez, RolSistema.ANALISTA_NEGOCIO, 100, 75_000));
		p.getMiembros().add(mp(ftorres, RolSistema.ARQUITECTO_SOFTWARE, 60, 110_000));
		p.getMiembros().add(mp(cvargas, RolSistema.DISENADOR_UI_UX, 80, 80_000));
		p.getMiembros().add(mp(dcastro, RolSistema.DESARROLLADOR_FRONTEND, 100, 90_000));
		p.getMiembros().add(mp(jsanchez, RolSistema.DESARROLLADOR_BACKEND, 100, 90_000));
		p.getMiembros().add(mp(dramirez, RolSistema.QA_ENGINEER, 70, 75_000));
		p.getMiembros().add(mp(inino, RolSistema.DEVOPS_ENGINEER, 40, 100_000));
		p = proyectoRepository.save(p);
		String pid = p.getId();

		Epica ep1 = saveEpica(pid, "Gestión de Pacientes", "COMPLETADA");
		Epica ep2 = saveEpica(pid, "Historia Clínica Digital", "EN_PROGRESO");
		Epica ep3 = saveEpica(pid, "Módulo de Citas Médicas", "PLANIFICADA");
		Epica ep4 = saveEpica(pid, "Facturación y Seguros", "PLANIFICADA");
		Epica ep5 = saveEpica(pid, "Reportes y Estadísticas", "PLANIFICADA");

		List<Sprint> sp = new ArrayList<>();
		sp.add(saveSprint(pid, 1, "Arquitectura base y gestión de pacientes", LocalDate.of(2025, 3, 10),
				LocalDate.of(2025, 3, 21), 160, "COMPLETADO"));
		sp.add(saveSprint(pid, 2, "Registro de consultas y diagnósticos", LocalDate.of(2025, 3, 24),
				LocalDate.of(2025, 4, 4), 160, "COMPLETADO"));
		sp.add(saveSprint(pid, 3, "Prescripciones y órdenes médicas", LocalDate.of(2025, 4, 7),
				LocalDate.of(2025, 4, 18), 160, "COMPLETADO"));
		sp.add(saveSprint(pid, 4, "Gestión de imágenes diagnósticas (DICOM básico)", LocalDate.of(2025, 4, 21),
				LocalDate.of(2025, 5, 2), 160, "COMPLETADO"));
		sp.add(saveSprint(pid, 5, "Historia clínica — continuación y alertas clínicas", LocalDate.of(2025, 5, 5),
				LocalDate.of(2025, 5, 16), 160, "ACTIVO"));
		sprintRepository.saveAll(sp);

		String sp5 = sp.get(4).getId();

		saveHu(pid, ep2.getId(), "Alertas de interacción medicamentosa", null, "MUST", 13, "EN_PROGRESO", sp5,
				List.of());
		saveHu(pid, ep2.getId(), "Firma digital del médico en historia clínica", null, "MUST", 8, "EN_REVISION", sp5,
				List.of());
		saveHu(pid, ep2.getId(), "Vista cronológica de historia clínica del paciente", null, "SHOULD", 8,
				"EN_PROGRESO", sp5, List.of());
		saveHu(pid, ep2.getId(), "Exportar historia clínica en PDF cifrado", null, "SHOULD", 8, "BACKLOG", sp5,
				List.of());
		saveHu(pid, ep2.getId(), "Control de acceso por especialidad médica", null, "MUST", 5, "ACEPTADA", sp5,
				List.of());

		saveHu(pid, ep3.getId(), "Módulo de agendamiento de citas", null, "MUST", 13, "BACKLOG", null, List.of());
		saveHu(pid, ep3.getId(), "Recordatorio automático de cita por SMS", null, "SHOULD", 5, "BACKLOG", null,
				List.of());
		saveHu(pid, ep3.getId(), "Cancelación y reprogramación de citas", null, "MUST", 8, "BACKLOG", null, List.of());
		saveHu(pid, ep3.getId(), "Lista de espera automática", null, "COULD", 5, "BACKLOG", null, List.of());

		saveHu(pid, ep4.getId(), "Facturación por acto médico", null, "MUST", 13, "BACKLOG", null, List.of());
		saveHu(pid, ep4.getId(), "Integración con aseguradoras EPS", null, "MUST", 13, "BACKLOG", null, List.of());
		saveHu(pid, ep4.getId(), "Copago y pagos parciales", null, "SHOULD", 8, "BACKLOG", null, List.of());

		saveHu(pid, ep5.getId(), "Dashboard de ocupación hospitalaria", null, "SHOULD", 8, "BACKLOG", null, List.of());
		saveHu(pid, ep5.getId(), "Reportes epidemiológicos por diagnóstico CIE-10", null, "MUST", 13, "BACKLOG", null,
				List.of());
		saveHu(pid, ep5.getId(), "Exportación de estadísticas a Excel", null, "SHOULD", 5, "BACKLOG", null, List.of());

		HistoriaUsuario hAlertas = findHuByTitle(pid, "Alertas de interacción medicamentosa");
		saveTarea(pid, hAlertas.getId(), sp5, "Implementar motor de reglas de interacciones", EstadosTarea.EN_PROGRESO,
				"BACKEND", jsanchez.getId(), jsanchez.nombreCompleto().trim(), "CRITICA");
		saveTarea(pid, hAlertas.getId(), sp5, "API endpoint /alertas/interacciones", EstadosTarea.EN_PROGRESO,
				"BACKEND", jsanchez.getId(), jsanchez.nombreCompleto().trim(), "ALTA");
		saveTarea(pid, hAlertas.getId(), sp5, "Unit tests motor de reglas", EstadosTarea.TODO, "QA",
				dramirez.getId(), dramirez.nombreCompleto().trim(), "MEDIA");

		saveRiesgo(pid, "Cumplimiento normativa Resolución 866 de 2021 (HC digital)", 3, 5, "MITIGANDO", cpinto.getId());
		saveRiesgo(pid, "Integración DICOM más compleja de lo estimado", 3, 4, "MITIGANDO", cpinto.getId());
		saveRiesgo(pid, "Disponibilidad de médicos para pruebas de aceptación", 4, 3, "IDENTIFICADO", cpinto.getId());

		saveDefecto(pid, "Formulario de consulta pierde datos al navegar atrás", "ALTA", "EN_DESARROLLO",
				dramirez.getId(), dcastro.getId());
		saveDefecto(pid, "PDF de prescripción no incluye sello médico", "MEDIA", "NUEVO", dramirez.getId(), null);
		saveDefecto(pid, "Búsqueda de paciente lenta (>3s) con >10.000 registros", "ALTA", "ASIGNADO", dramirez.getId(),
				jsanchez.getId());

		log.info("Seed proyecto Historia Clínica completado.");
	}

	private void seedAlkosto(Map<String, Usuario> u, Cliente cli) {
		Usuario vrueda = require(u, "v.rueda@sigprod.com");
		Usuario lgomez = require(u, "l.gomez@sigprod.com");
		Usuario smartinez = require(u, "s.martinez@sigprod.com");
		Usuario ftorres = require(u, "f.torres@sigprod.com");
		Usuario cvargas = require(u, "c.vargas@sigprod.com");
		Usuario smora = require(u, "s.mora@sigprod.com");
		Usuario dcastro = require(u, "d.castro@sigprod.com");
		Usuario malvarez = require(u, "m.alvarez@sigprod.com");
		Usuario jsanchez = require(u, "j.sanchez@sigprod.com");
		Usuario dramirez = require(u, "d.ramirez@sigprod.com");
		Usuario inino = require(u, "i.nino@sigprod.com");

		Proyecto p = new Proyecto();
		p.setNombre("Plataforma E-commerce Alkosto Digital");
		p.setDescripcion("Marketplace y comercio electrónico multicanal.");
		p.setClienteId(cli.getId());
		p.setClienteNombre(cli.getRazonSocial());
		p.setProductOwnerId(vrueda.getId());
		p.setProductOwnerNombre(vrueda.nombreCompleto().trim());
		p.setProjectManagerId(lgomez.getId());
		p.setProjectManagerNombre(lgomez.nombreCompleto().trim());
		p.setMetodologia("SCRUM");
		p.setEstado("ACTIVO");
		p.setPresupuesto(650_000_000d);
		p.setFechaInicio(LocalDate.of(2025, 5, 5));
		p.setFechaFin(LocalDate.of(2026, 4, 30));
		p.setProgresoPorcentaje(5);
		p.setTotalPuntosProducto(520);
		List<Double> z = new ArrayList<>();
		List<Double> br = new ArrayList<>();
		for (int i = 0; i < 12; i++) {
			z.add(0d);
			br.add(520d);
		}
		p.setBurnupAcumulado(z);
		p.setBurndownRestante(br);
		p.setEtiquetasBurnSprints(Arrays.asList("Inicio", "S1", "S2", "S3", "S4", "S5", "S6", "S7", "S8", "S9", "S10",
				"S11"));
		p.getMiembros().add(mp(vrueda, RolSistema.PRODUCT_OWNER, 50, 85_000));
		p.getMiembros().add(mp(lgomez, RolSistema.PROJECT_MANAGER, 80, 95_000));
		p.getMiembros().add(mp(smartinez, RolSistema.ANALISTA_NEGOCIO, 100, 75_000));
		p.getMiembros().add(mp(ftorres, RolSistema.ARQUITECTO_SOFTWARE, 80, 110_000));
		p.getMiembros().add(mp(cvargas, RolSistema.DISENADOR_UI_UX, 100, 80_000));
		p.getMiembros().add(mp(smora, RolSistema.DESARROLLADOR_FRONTEND, 100, 90_000));
		p.getMiembros().add(mp(dcastro, RolSistema.DESARROLLADOR_FRONTEND, 100, 90_000));
		p.getMiembros().add(mp(malvarez, RolSistema.DESARROLLADOR_BACKEND, 100, 90_000));
		p.getMiembros().add(mp(jsanchez, RolSistema.DESARROLLADOR_BACKEND, 100, 90_000));
		p.getMiembros().add(mp(dramirez, RolSistema.QA_ENGINEER, 100, 75_000));
		p.getMiembros().add(mp(inino, RolSistema.DEVOPS_ENGINEER, 100, 100_000));
		p = proyectoRepository.save(p);
		String pid = p.getId();

		String[] titulosEp = { "Catálogo de Productos", "Carrito de Compras y Checkout", "Gestión de Usuarios y Perfiles",
				"Pagos y Pasarelas", "Gestión de Pedidos y Logística", "Motor de Búsqueda y Recomendaciones",
				"Panel de Vendedores (Marketplace)", "Notificaciones y Marketing", "Panel Administrativo",
				"Analytics y Reportes" };
		List<Epica> epicas = new ArrayList<>();
		for (String t : titulosEp) {
			epicas.add(saveEpica(pid, t, "PLANIFICADA"));
		}

		LocalDate cursor = LocalDate.of(2025, 5, 19);
		for (int sn = 1; sn <= 11; sn++) {
			LocalDate ini = cursor;
			LocalDate fin = cursor.plusWeeks(2).minusDays(1);
			saveSprint(pid, sn, "Sprint planificado " + sn, ini, fin, 240, "PLANIFICADO");
			cursor = cursor.plusWeeks(2);
		}

		Object[][] backlog = alkostoHistorias();
		for (Object[] row : backlog) {
			String titulo = (String) row[0];
			String moscow = (String) row[1];
			int pts = (Integer) row[2];
			String estado = (String) row[3];
			int epIdx = (Integer) row[4];
			saveHu(pid, epicas.get(epIdx).getId(), titulo, null, moscow, pts, estado, null, List.of());
		}

		saveRiesgo(pid, "Integración DIAN para facturación electrónica requiere certificado digital", 4, 5,
				"IDENTIFICADO", lgomez.getId());
		saveRiesgo(pid, "Volumen de catálogo (>500.000 productos) puede afectar rendimiento BD", 3, 5, "IDENTIFICADO",
				lgomez.getId());
		saveRiesgo(pid, "Normativa PCI-DSS para manejo de pagos con tarjeta", 4, 5, "IDENTIFICADO", lgomez.getId());
		saveRiesgo(pid, "Curva de aprendizaje Elasticsearch del equipo", 3, 3, "IDENTIFICADO", lgomez.getId());

		log.info("Seed proyecto Alkosto completado.");
	}

	/** Filas: título, MoSCoW, puntos, estado, índice épica 0..9 */
	private Object[][] alkostoHistorias() {
		List<Object[]> rows = new ArrayList<>();
		int e1 = 0, e2 = 1, e3 = 2, e4 = 3, e5 = 4, e6 = 5, e7 = 6, e8 = 7, e9 = 8, e9b = 8, e10 = 9;
		rows.add(new Object[] { "Registro de usuario con email y contraseña", "MUST", 8, "BACKLOG", e3 });
		rows.add(new Object[] { "Login con redes sociales (Google, Facebook)", "SHOULD", 13, "BACKLOG", e3 });
		rows.add(new Object[] { "Listado de productos con paginación", "MUST", 8, "BACKLOG", e1 });
		rows.add(new Object[] { "Ficha de producto — galería, precio, descripción", "MUST", 13, "BACKLOG", e1 });
		rows.add(new Object[] { "Categorías y subcategorías de productos", "MUST", 8, "BACKLOG", e1 });
		rows.add(new Object[] { "Búsqueda simple por nombre de producto", "MUST", 5, "BACKLOG", e1 });
		rows.add(new Object[] { "Variantes de producto (talla, color, capacidad)", "MUST", 13, "BACKLOG", e1 });
		rows.add(new Object[] { "Gestión de inventario en tiempo real", "MUST", 8, "BACKLOG", e1 });
		rows.add(new Object[] { "Carga masiva de productos por CSV", "SHOULD", 8, "BACKLOG", e1 });
		rows.add(new Object[] { "Sistema de valoraciones y reseñas", "SHOULD", 8, "BACKLOG", e1 });
		rows.add(new Object[] { "Productos relacionados y 'también compraron'", "COULD", 5, "BACKLOG", e1 });
		rows.add(new Object[] { "Agregar producto al carrito", "MUST", 8, "BACKLOG", e2 });
		rows.add(new Object[] { "Actualizar cantidad en carrito", "MUST", 3, "BACKLOG", e2 });
		rows.add(new Object[] { "Guardar carrito para usuarios registrados", "SHOULD", 5, "BACKLOG", e2 });
		rows.add(new Object[] { "Lista de deseos (Wishlist)", "COULD", 5, "BACKLOG", e2 });
		rows.add(new Object[] { "Aplicar cupones de descuento", "SHOULD", 8, "BACKLOG", e2 });
		rows.add(new Object[] { "Calculadora de costos de envío", "MUST", 5, "BACKLOG", e2 });
		rows.add(new Object[] { "Flujo de checkout en 3 pasos", "MUST", 13, "BACKLOG", e2 });
		rows.add(new Object[] { "Pago con tarjeta crédito/débito (Wompi)", "MUST", 13, "BACKLOG", e4 });
		rows.add(new Object[] { "Confirmación de pedido por email", "MUST", 5, "BACKLOG", e2 });
		rows.add(new Object[] { "Gestión de direcciones de envío", "MUST", 8, "BACKLOG", e2 });
		rows.add(new Object[] { "Pago PSE Bancolombia", "MUST", 8, "BACKLOG", e4 });
		rows.add(new Object[] { "Pago contraentrega", "SHOULD", 5, "BACKLOG", e4 });
		rows.add(new Object[] { "Dashboard de pedidos del cliente", "MUST", 8, "BACKLOG", e5 });
		rows.add(new Object[] { "Seguimiento de pedido en tiempo real", "MUST", 13, "BACKLOG", e5 });
		rows.add(new Object[] { "Política de devoluciones — solicitud online", "SHOULD", 8, "BACKLOG", e5 });
		rows.add(new Object[] { "Integración con Servientrega y Coordinadora", "MUST", 13, "BACKLOG", e5 });
		rows.add(new Object[] { "Gestión de bodegas y stock por ciudad", "SHOULD", 8, "BACKLOG", e5 });
		rows.add(new Object[] { "Alertas de stock bajo", "SHOULD", 5, "BACKLOG", e5 });
		rows.add(new Object[] { "Factura electrónica DIAN", "MUST", 13, "BACKLOG", e5 });
		rows.add(new Object[] { "Búsqueda avanzada con filtros (precio, marca, rating)", "MUST", 13, "BACKLOG", e6 });
		rows.add(new Object[] { "Autocompletado en buscador", "SHOULD", 8, "BACKLOG", e6 });
		rows.add(new Object[] { "Búsqueda por voz", "COULD", 8, "BACKLOG", e6 });
		rows.add(new Object[] { "Ordenamiento por relevancia, precio, novedad", "MUST", 5, "BACKLOG", e6 });
		rows.add(new Object[] { "Motor de recomendaciones ML básico", "SHOULD", 13, "BACKLOG", e6 });
		rows.add(new Object[] { "Registro de vendedor marketplace", "MUST", 8, "BACKLOG", e7 });
		rows.add(new Object[] { "Dashboard de ventas para vendedor", "MUST", 13, "BACKLOG", e7 });
		rows.add(new Object[] { "Gestión de comisiones por venta", "MUST", 8, "BACKLOG", e7 });
		rows.add(new Object[] { "Notificaciones push web y móvil", "SHOULD", 8, "BACKLOG", e8 });
		rows.add(new Object[] { "Email transaccional (bienvenida, compra, envío)", "MUST", 8, "BACKLOG", e8 });
		rows.add(new Object[] { "Newsletter y campañas de email marketing", "COULD", 5, "BACKLOG", e8 });
		rows.add(new Object[] { "Panel admin — gestión de usuarios y productos", "MUST", 13, "BACKLOG", e9b });
		rows.add(new Object[] { "Dashboard de analytics — ventas, conversión, ticket promedio", "MUST", 13, "BACKLOG",
				e10 });
		rows.add(new Object[] { "Reportes de ventas por categoría/período", "MUST", 8, "BACKLOG", e10 });
		rows.add(new Object[] { "Pruebas de carga (JMeter, 10.000 usuarios concurrentes)", "MUST", 8, "BACKLOG", e10 });
		rows.add(new Object[] { "Optimización SEO — metadatos dinámicos", "SHOULD", 5, "BACKLOG", e10 });
		rows.add(new Object[] { "PWA — experiencia offline básica", "COULD", 13, "BACKLOG", e10 });
		return rows.toArray(new Object[0][]);
	}

	private Epica saveEpica(String proyectoId, String titulo, String estado) {
		Epica e = new Epica();
		e.setProyectoId(proyectoId);
		e.setTitulo(titulo);
		e.setEstado(estado);
		e.setPrioridad("ALTA");
		return epicaRepository.save(e);
	}

	private Sprint saveSprint(String proyectoId, int num, String objetivo, LocalDate ini, LocalDate fin, int cap,
			String estado) {
		Sprint s = new Sprint();
		s.setProyectoId(proyectoId);
		s.setNumero(num);
		s.setObjetivo(objetivo);
		s.setFechaInicio(ini);
		s.setFechaFin(fin);
		s.setCapacidadHoras(cap);
		s.setEstado(estado);
		return sprintRepository.save(s);
	}

	private void saveHu(String proyectoId, String epicaId, String titulo, String narrativa, String moscow, int pts,
			String estado, String sprintId, List<CriterioAceptacion> criterios) {
		HistoriaUsuario h = new HistoriaUsuario();
		h.setProyectoId(proyectoId);
		h.setEpicaId(epicaId);
		h.setTitulo(titulo);
		h.setNarrativa(narrativa);
		h.setPrioridadMoscow(moscow);
		h.setEstimacionPuntos(pts);
		h.setEstado(estado);
		h.setSprintId(sprintId);
		h.setValorNegocio(5);
		h.getCriteriosAceptacion().addAll(criterios);
		historiaUsuarioRepository.save(h);
	}

	private void saveTarea(String proyectoId, String historiaId, String sprintId, String titulo, String estado,
			String tipo, String asignadoId, String asignadoNombre, String prioridad) {
		Tarea t = new Tarea();
		t.setProyectoId(proyectoId);
		t.setHistoriaId(historiaId);
		t.setSprintId(sprintId);
		t.setTitulo(titulo);
		t.setEstado(estado);
		t.setTipo(tipo);
		t.setPrioridad(prioridad);
		t.setAsignadoId(asignadoId);
		t.setAsignadoNombre(asignadoNombre);
		t.setFechaLimite(LocalDate.now().plusDays(14));
		if (EstadosTarea.COMPLETADA.equals(estado)) {
			t.setHorasEstimadas(8d);
			t.setHorasReales(8d);
		} else {
			t.setHorasEstimadas(6d);
		}
		tareaRepository.save(t);
	}

	private void saveRiesgo(String proyectoId, String desc, int prob, int impacto, String estado, String resp) {
		Riesgo r = new Riesgo();
		r.setProyectoId(proyectoId);
		r.setDescripcion(desc);
		r.setProbabilidad(prob);
		r.setImpacto(impacto);
		r.setEstado(estado);
		r.setResponsableId(resp);
		r.setPlanMitigacion("Plan definido en comité de riesgos.");
		riesgoRepository.save(r);
	}

	private void saveDefecto(String proyectoId, String titulo, String severidad, String estado, String reporta,
			String asignado) {
		Defecto d = new Defecto();
		d.setProyectoId(proyectoId);
		d.setTitulo(titulo);
		d.setSeveridad(severidad);
		d.setEstado(estado);
		d.setPrioridad(severidad);
		d.setReportadoPorId(reporta);
		d.setAsignadoAId(asignado);
		d.setFechaReporte(LocalDateTime.now());
		defectoRepository.save(d);
	}
}
