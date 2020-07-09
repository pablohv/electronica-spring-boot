package com.electronica.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.electronica.app.models.entity.Factura;

public interface IFacturaService {
	
	public List<Factura> ventas();
	
	public Page<Factura> ventas(Pageable pageable);
	
	public void saveFactura(Factura factura);

	public Factura findFacturaById(Long id);
	
	public void deleteFactura(Long id);
	
	public Factura fetchFacturaByIdWithClienteWhithItemFacturaWithProducto(Long id);

}
