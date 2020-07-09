package com.electronica.app.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.electronica.app.models.entity.Cliente;
import com.electronica.app.models.service.iClienteService;
import com.electronica.app.models.util.paginator.PageRender;

@Controller
@RequestMapping("/cliente")
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private iClienteService clienteService;

	@GetMapping(value = "/listar")
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		
		Pageable pageRequest = PageRequest.of(page, 15);

		Page<Cliente> clientes = clienteService.finAll(pageRequest);
		
		PageRender<Cliente> pageRender = new PageRender<Cliente>("/cliente/listar", clientes);

		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);

		return "cliente/listar";
	}

	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Model model) {
		Cliente cliente = clienteService.fetchByIdWithFacturas(id);
		
		Cliente cliente2 = clienteService.fetchByIdWithReparaciones(id);

		if (cliente == null) {
			return "redirect:/cliente/listar";
		}
		
		if (cliente2 == null) {
			return "redirect:/cliente/listar";
		}

		model.addAttribute("titulo", "Detalle ".concat(cliente.getNombre()));
		model.addAttribute("cliente", cliente);
		model.addAttribute("cliente2", cliente2);

		return "cliente/ver";

	}

	@GetMapping(value = "/form")
	public String crear(Model model) {
		Cliente cliente = new Cliente();

		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", "Crear cliente");

		return "cliente/form";
	}

	@PostMapping(value = "/form")
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, RedirectAttributes flash,
			SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Crear cliente");
			return "cliente/form";
		}

		String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con éxito!" : "Cliente creado con éxito!";

		clienteService.save(cliente);

		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);

		return "redirect:/cliente/listar";
	}

	@GetMapping(value = "/editar/{id}")
	public String editar(@PathVariable(value = "id") Long id, Model model) {
		Cliente cliente = null;

		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				return "redirect:/cliente/listar";
			}
		} else {
			return "redirect:/cliente/listar";
		}

		model.addAttribute("titulo", "Editar cliente");
		model.addAttribute("cliente", cliente);

		return "cliente/form";
	}

	@GetMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

		if (id > 0) {
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente '" + id + "' eliminado con exito");
		}

		return "redirect:/cliente/listar";
	}

}
