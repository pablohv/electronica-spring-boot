package com.electronica.app.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.electronica.app.models.entity.Producto;
import com.electronica.app.models.service.IProductoService;

@Controller
public class IndexController {
	
	@Autowired
	private IProductoService productoService;
	
	@RequestMapping("/")
	public String index(Model model) {
		
		List<Producto> productos = productoService.findByCantidad();
		
		String[] objetos = productoService.findByMasVendido();
		
		List<Producto> masVendido = new ArrayList<>(); 
		
		for (int i = 0; i < objetos.length; i++) {
			String parte = objetos[i];
			String [] parts = parte.split(",");
			Producto produ = new Producto();
			
			produ.setId(Long.valueOf(parts[0]));
			produ.setNombre(parts[1]);
			produ.setCantidad(Integer.parseInt(parts[2]));
			
			masVendido.add(produ);
		}
		
		model.addAttribute("titulo", "Bienvenido");
		model.addAttribute("productos", productos);
		model.addAttribute("objetos", masVendido);
		
		return "index";
	}

}
