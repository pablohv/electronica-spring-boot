package com.electronica.app.controllers;

import java.util.Date;

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
import com.electronica.app.models.entity.Reparacion;
import com.electronica.app.models.service.IReparacionService;
import com.electronica.app.models.service.iClienteService;
import com.electronica.app.models.util.paginator.PageRender;

@Controller
@RequestMapping(value = "/reparacion")
@SessionAttributes("reparacion")
public class ReparacionController {

	@Autowired
	private IReparacionService reparacionService;

	@Autowired
	private iClienteService clienteService;

	@RequestMapping(value = "/listar")
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Reparacion> reparaciones = reparacionService.findAll(pageRequest);
		PageRender<Reparacion> pageRender = new PageRender<Reparacion>("/reparacion/listar", reparaciones);

		model.addAttribute("titulo", "Listado reparaciones");
		model.addAttribute("reparaciones", reparaciones);
		model.addAttribute("page", pageRender);

		return "reparacion/listar";
	}

	@GetMapping(value = "/form/{id}")
	public String crear(@PathVariable(value = "id") Long id, Model model) {
		Cliente cliente = clienteService.findOne(id);

		if (cliente == null) {
			return "redirect:/cliente/listar";
		}

		Reparacion reparacion = new Reparacion();
		reparacion.setCliente(cliente);

		model.addAttribute("reparacion", reparacion);
		model.addAttribute("titulo", "Agregar reparacion");

		return "reparacion/form";
	}

	@PostMapping(value = "/form")
	public String guardar(@Valid Reparacion reparacion, BindingResult result, SessionStatus status,
			Model model, RedirectAttributes flash) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Agregar repracion");
			return "reparacion/form";
		}
		
		reparacionService.save(reparacion);
		status.setComplete();
		
	    flash.addAttribute("success", "Reparacion agregada con exito");

		return "redirect:/cliente/ver/".concat(reparacion.getCliente().getId().toString());
	}

	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Model model) {

		Reparacion reparacion = reparacionService.findOne(id);

		if (reparacion == null) {
			return "redirect:/reparacion/listar";
		}

		model.addAttribute("reparacion", reparacion);
		model.addAttribute("titulo", "Reparacion detalle");

		return "reparacion/ver";
	}

	@GetMapping(value = "/entregar/{id}")
	public String entregar(@PathVariable(value = "id") Long id) {

		Reparacion reparacion = reparacionService.findOne(id);

		if (reparacion == null) {
			return "redirect:/reparacion/listar";
		}

		Date date = new Date();
		reparacion.setFechaEntrega(date);
		reparacionService.save(reparacion);

		return "redirect:/reparacion/ver/".concat(reparacion.getId().toString());
	}

}
