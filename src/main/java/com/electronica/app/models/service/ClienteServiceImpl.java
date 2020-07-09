package com.electronica.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.electronica.app.models.dao.IClienteDao;
import com.electronica.app.models.entity.Cliente;

@Service
public class ClienteServiceImpl implements iClienteService{
	
	@Autowired
	private IClienteDao clienteDao;

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> finAll() {
		return (List<Cliente>) clienteDao.findAll();
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {
		clienteDao.save(cliente);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findOne(Long id) {
		return clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		clienteDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente fetchByIdWithFacturas(Long id) {
		return clienteDao.fetchByIdWithFacturas(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente fetchByIdWithReparaciones(Long id) {
		return clienteDao.fetchByIdWithReparaciones(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> finAll(Pageable pageable) {
		return clienteDao.findAll(pageable);
	}

}
