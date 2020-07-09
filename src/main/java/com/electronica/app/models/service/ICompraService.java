package com.electronica.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.electronica.app.models.entity.Compra;

public interface ICompraService {
	
	public List<Compra> findAll();
	
	public Page<Compra> findAll(Pageable pageable);
	
	public void save(Compra compra);
	
	public Compra findOne(Long id);
	
	public void delete(Long id); 
	
	public Compra fetchByIdWhithItemCompraWithProducto(Long id); 

}
