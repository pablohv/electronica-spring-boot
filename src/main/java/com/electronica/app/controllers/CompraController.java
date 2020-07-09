package com.electronica.app.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.electronica.app.models.entity.Compra;
import com.electronica.app.models.entity.ItemCompra;
import com.electronica.app.models.entity.Producto;
import com.electronica.app.models.service.ICompraService;
import com.electronica.app.models.service.IProductoService;
import com.electronica.app.models.util.paginator.PageRender;

@Controller
@SessionAttributes("compra")
@RequestMapping(value = "/compra")
public class CompraController {

	@Autowired
	private ICompraService compraService;
	
	@Autowired
	private IProductoService productoService;
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	@RequestMapping(value = "/listar")
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		Pageable pageRequest = PageRequest.of(page, 15);
		Page<Compra> compras = compraService.findAll(pageRequest);
		
		PageRender<Compra> pageRender = new PageRender<Compra>("/compra/listar", compras);

		model.addAttribute("titulo", "Listado compras");
		model.addAttribute("compras", compras);
		model.addAttribute("page", pageRender);

		return "compra/listar";
	}

	@RequestMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Model model) {
		Compra compra = compraService.fetchByIdWhithItemCompraWithProducto(id);

		if (compra == null) {
			return "redirect:/compra/listar";
		}

		model.addAttribute("titulo", "Compra ".concat(compra.getId().toString()));
		model.addAttribute("compra", compra);

		return "compra/ver";
	}

	@GetMapping(value = "/form")
	public String crear(Model model) {
		Compra compra = new Compra();

		model.addAttribute("compra", compra);
		model.addAttribute("titulo", "Crear compra");

		return "compra/form";
	}

	@PostMapping(value = "/form")
	public String guardar(@Valid Compra compra, BindingResult result, Model model, 
			@RequestParam(name = "item_id[]", required = false) Long[] itemId,
			@RequestParam(name = "cantidad[]", required = false) Integer[] cantidad, 
			SessionStatus status, RedirectAttributes flash) {
		
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Crear cliente");
			model.addAttribute("error", "Error: Hay un campo vacio");
			return "compra/form";
		}

		if (itemId == null || itemId.length == 0) {
			model.addAttribute("titulo", "Crear Compra");
			model.addAttribute("error", "Error: La Compra NO puede no tener líneas!");
			return "compra/form";
		}

		for (int i = 0; i < itemId.length; i++) {
			Producto producto = productoService.findProductoById(itemId[i]);

			ItemCompra linea = new ItemCompra();
			linea.setCantidad(cantidad[i]);
			linea.setProducto(producto);
			compra.addItemCompra(linea);

			log.info("ID: " + itemId[i].toString() + ", cantidad: " + cantidad[i].toString());

			int nuevaCantidad = producto.getCantidad() + cantidad[i];
			producto.setCantidad(nuevaCantidad);
			productoService.save(producto);

		}

		compraService.save(compra);
		status.setComplete();
		flash.addAttribute("success", "Compra agregada con exito");

		return "redirect:/compra/listar";
	}

}
