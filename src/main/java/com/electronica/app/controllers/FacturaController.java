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

import com.electronica.app.models.entity.Producto;
import com.electronica.app.models.entity.ItemFactura;
import com.electronica.app.models.util.paginator.PageRender;
import com.electronica.app.models.entity.Cliente;
import com.electronica.app.models.entity.Factura;
import com.electronica.app.models.service.IFacturaService;
import com.electronica.app.models.service.IProductoService;
import com.electronica.app.models.service.iClienteService;

@Controller
@RequestMapping(value = "/factura")
@SessionAttributes("factura")
public class FacturaController {
	
	@Autowired
	private iClienteService clienteService;
	
	@Autowired
	private IProductoService productoService;
	
	@Autowired
	private IFacturaService facturaService;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable (value = "id") Long id, Model model) {
		Factura factura = facturaService.fetchFacturaByIdWithClienteWhithItemFacturaWithProducto(id);
		
		if (factura == null) {
			return "redirect:/cliente/listar";
		}
		
		model.addAttribute("factura", factura);
		model.addAttribute("titulo", "Factura: ".concat(factura.getId().toString()));
		
		return "factura/ver";
	}
	
	@GetMapping(value = "/form/{idCliente}")
	public String crear(@PathVariable(value = "idCliente") Long idCliente, Model model) {
		Cliente cliente = clienteService.findOne(idCliente);
		
		if (cliente == null) {
			return "redirect:/cliente/listar";
		}
		
		Factura factura = new Factura();
		factura.setCliente(cliente);
		
		model.addAttribute("factura", factura);
		model.addAttribute("titulo", "Crear factura");
		
		return "factura/form";
	}
	
	@PostMapping(value = "/form")
	public String guardar(@Valid Factura factura, BindingResult result, Model model, 
			@RequestParam(name = "item_id[]", required = false) Long[] itemId,
			@RequestParam(name = "cantidad[]", required = false) Integer[] cantidad, 
			SessionStatus status, RedirectAttributes flash) {
		
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Te falto agregar un campo");
			return "factura/form";
		}
		
		if (itemId == null || itemId.length == 0) {
			model.addAttribute("titulo", "Crear Factura");
			model.addAttribute("error", "Error: La factura NO puede no tener líneas!");
			return "factura/form";
		}
		
		for (int i = 0; i < itemId.length; i++) {
			Producto producto = productoService.findProductoById(itemId[i]);
			
			if (producto.getCantidad() < cantidad[i]) {
				model.addAttribute("error", "El producto " + producto.getNombre() + " solo tiene disponibles " + producto.getCantidad());
				model.addAttribute("titulo", "Crear Factura");
				return "factura/form";
			}
		}
		
		for (int i = 0; i < itemId.length; i++) {
			Producto producto = productoService.findProductoById(itemId[i]);

			ItemFactura linea = new ItemFactura();
			linea.setCantidad(cantidad[i]);
			linea.setProducto(producto);
			factura.addItemFactura(linea);
			
			log.info("ID: " + itemId[i].toString() + ", cantidad: " + cantidad[i].toString());
			
			int nuevaCantidad = producto.getCantidad() - cantidad[i];
			producto.setCantidad(nuevaCantidad);
			productoService.save(producto);

		}
		
		facturaService.saveFactura(factura);
		status.setComplete();
		
		flash.addFlashAttribute("success", "Factura/Compra agregada con exito");
		
		return "redirect:/cliente/ver/".concat(factura.getCliente().getId().toString());
	}
	
	@RequestMapping(value = "/ventas")
	public String ventas(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		Pageable pageRequest = PageRequest.of(page, 15);

		Page<Factura> compras = facturaService.ventas(pageRequest);
		
		PageRender<Factura> pageRender = new PageRender<Factura>("/factura/ventas", compras);
		
		model.addAttribute("titulo", "Listado ventas");
		model.addAttribute("compras", compras);
		model.addAttribute("page", pageRender);
		
		return "venta/listar";
	}
	

}
