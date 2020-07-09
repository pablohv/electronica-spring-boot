package com.electronica.app.controllers;

import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.electronica.app.models.util.paginator.PageRender;
import com.electronica.app.models.entity.Producto;
import com.electronica.app.models.service.IProductoService;

@Controller
@RequestMapping("/producto")
@SessionAttributes("producto")
public class ProductoController {

	@Autowired
	private IProductoService productoService;

	@GetMapping(value = "/listar")
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		
		Pageable pageRequest = PageRequest.of(page, 15);
		
		Page<Producto> productos = productoService.findAll(pageRequest);

		PageRender<Producto> pageRender = new PageRender<Producto>("/producto/listar", productos);
		
		model.addAttribute("productos", productos);
		model.addAttribute("titulo", "Listado de productos");
		model.addAttribute("page", pageRender);
		
		return "producto/listar";
	}

	@GetMapping(value = "/form")
	public String crear(Model model) {
		Producto producto = new Producto();

		model.addAttribute("producto", producto);
		model.addAttribute("titulo", "Agregar producto");

		return "producto/form";
	}
	
	@GetMapping(value = "/cargar-productos/{term}", produces = { "application/json" })
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
		return productoService.findByNombre(term);
	}

	@PostMapping(value = "/form")
	public String guardar(@Valid Producto producto, BindingResult result, Model model,
			SessionStatus status, RedirectAttributes flash) {
		
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Agregar producto");
			return "producto/form";
		}
		
		productoService.save(producto);
		status.setComplete();
		
		flash.addFlashAttribute("success", "Producto agregado con exito");

		return "redirect:/producto/listar";
	}

	@GetMapping(value = "/editar/{id}")
	public String editar(@PathVariable(value = "id") Long id, Model model) {
		Producto producto = null;

		if (id > 0) {
			producto = productoService.findOne(id);
			if (producto == null) {
				return "redirect:producto/listar";
			}
		} else {
			return "redirect:producto/listar";
		}

		model.addAttribute("titulo", "Editar producto");
		model.addAttribute("producto", producto);

		return "producto/form";
	}
	
	@GetMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id) {
		productoService.delete(id);
		
		return "redirect:/producto/listar";
	}

}
