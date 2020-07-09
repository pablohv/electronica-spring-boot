package com.electronica.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.electronica.app.models.dao.ICompraDao;
import com.electronica.app.models.entity.Compra;

@Service
public class CompraServiceImpl implements ICompraService {

	@Autowired
	private ICompraDao compraDao;

	@Override
	@Transactional(readOnly = true)
	public List<Compra> findAll() {
		return (List<Compra>) compraDao.findAll();
	}

	@Override
	@Transactional
	public void save(Compra compra) {
		compraDao.save(compra);
	}

	@Override
	@Transactional(readOnly = true)
	public Compra findOne(Long id) {
		return compraDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		compraDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Compra fetchByIdWhithItemCompraWithProducto(Long id) {
		return compraDao.fetchByIdWhithItemCompraWithProducto(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Compra> findAll(Pageable pageable) {
		return compraDao.findAll(pageable);
	}

}
