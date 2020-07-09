package com.electronica.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.electronica.app.models.dao.IFacturaDao;
import com.electronica.app.models.entity.Factura;

@Service
public class FacturaServiceImpl implements IFacturaService {
	
	@Autowired
	private IFacturaDao facturaDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Factura> ventas(){
		return (List<Factura>) facturaDao.findAll();
	}
	
	@Override
	@Transactional
	public void saveFactura(Factura factura) {
		facturaDao.save(factura);
	}

	@Override
	@Transactional(readOnly = true)
	public Factura findFacturaById(Long id) {
		return facturaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteFactura(Long id) {
		facturaDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Factura fetchFacturaByIdWithClienteWhithItemFacturaWithProducto(Long id) {
		return facturaDao.fetchByIdWithClienteWhithItemFacturaWithProducto(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Factura> ventas(Pageable pageable) {
		return facturaDao.findAll(pageable);
	}
	
}
