package com.electronica.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.electronica.app.models.dao.IReparacionDao;
import com.electronica.app.models.entity.Reparacion;

@Service
public class ReparacionServiceImpl implements IReparacionService {
	
	@Autowired
	private IReparacionDao reparacionDao;

	@Override
	@Transactional(readOnly = true)
	public List<Reparacion> findAll() {
		return (List<Reparacion>) reparacionDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Reparacion findOne(Long id) {
		return reparacionDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void save(Reparacion reparacion) {
		reparacionDao.save(reparacion);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Reparacion> findAll(Pageable pageable) {
		return reparacionDao.findAll(pageable);
	}

}
