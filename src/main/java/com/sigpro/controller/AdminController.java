package com.sigpro.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sigpro.model.Cliente;
import com.sigpro.model.RolSistema;
import com.sigpro.model.Usuario;
import com.sigpro.service.AdminResumenService;
import com.sigpro.service.ClienteService;
import com.sigpro.service.UsuarioService;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AdminController {

	private final UsuarioService usuarioService;
	private final ClienteService clienteService;
	private final AdminResumenService adminResumenService;

	public AdminController(UsuarioService usuarioService, ClienteService clienteService,
			AdminResumenService adminResumenService) {
		this.usuarioService = usuarioService;
		this.clienteService = clienteService;
		this.adminResumenService = adminResumenService;
	}

	@GetMapping({"", "/"})
	public String raizAdmin() {
		return "redirect:/admin/resumen";
	}

	@GetMapping("/resumen")
	public String resumen(Model model) {
		model.addAttribute("conteos", adminResumenService.conteosGlobales());
		model.addAttribute("historiasPorEstado", adminResumenService.historiasPorEstado());
		model.addAttribute("filasProyecto", adminResumenService.filasPorProyecto());
		return "admin/resumen";
	}

	@GetMapping("/usuarios")
	public String usuarios(Model model) {
		List<Usuario> usuarios = usuarioService.listarTodos();
		model.addAttribute("usuarios", usuarios);
		model.addAttribute("roles", RolSistema.values());
		return "admin/usuarios";
	}

	@PostMapping("/usuarios/{id}/estado")
	public String cambiarEstado(@PathVariable String id, @RequestParam String estado, RedirectAttributes ra) {
		Usuario u = usuarioService.findById(id).orElseThrow();
		usuarioService.actualizarRolYEstado(id, u.getRol(), estado);
		ra.addFlashAttribute("msg", "Usuario actualizado.");
		return "redirect:/admin/usuarios";
	}

	@PostMapping("/usuarios/{id}/rol")
	public String cambiarRol(@PathVariable String id, @RequestParam RolSistema rol, RedirectAttributes ra) {
		Usuario u = usuarioService.findById(id).orElseThrow();
		usuarioService.actualizarRolYEstado(id, rol, u.getEstado());
		ra.addFlashAttribute("msg", "Rol actualizado.");
		return "redirect:/admin/usuarios";
	}

	@GetMapping("/clientes")
	public String clientes(Model model) {
		model.addAttribute("clientes", clienteService.listar());
		model.addAttribute("cliente", new Cliente());
		model.addAttribute("editando", false);
		return "admin/clientes";
	}

	@GetMapping("/clientes/{id}/editar")
	public String editarCliente(@PathVariable String id, Model model, RedirectAttributes ra) {
		return clienteService.findById(id).map(c -> {
			model.addAttribute("clientes", clienteService.listar());
			model.addAttribute("cliente", c);
			model.addAttribute("editando", true);
			return "admin/clientes";
		}).orElseGet(() -> {
			ra.addFlashAttribute("msg", "Cliente no encontrado.");
			return "redirect:/admin/clientes";
		});
	}

	@PostMapping("/clientes")
	public String guardarCliente(Cliente cliente, RedirectAttributes ra) {
		boolean nuevo = cliente.getId() == null || cliente.getId().isBlank();
		clienteService.guardar(cliente);
		ra.addFlashAttribute("msg", nuevo ? "Cliente creado." : "Cliente actualizado.");
		return "redirect:/admin/clientes";
	}

	@PostMapping("/clientes/{id}/eliminar")
	public String eliminarCliente(@PathVariable String id, RedirectAttributes ra) {
		clienteService.eliminar(id);
		ra.addFlashAttribute("msg", "Cliente eliminado.");
		return "redirect:/admin/clientes";
	}
}
