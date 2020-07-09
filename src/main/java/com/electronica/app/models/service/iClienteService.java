package com.electronica.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.electronica.app.models.entity.Cliente;

public interface iClienteService {
	
	public List<Cliente> finAll();
	
	public Page<Cliente> finAll(Pageable pageable);
	
	public void save(Cliente cliente);
	
	public Cliente findOne(Long id);
	
	public void delete(Long id);
	
	public Cliente fetchByIdWithFacturas(Long id);
	
	public Cliente fetchByIdWithReparaciones(Long id);

}
