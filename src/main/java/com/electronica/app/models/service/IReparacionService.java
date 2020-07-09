package com.electronica.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.electronica.app.models.entity.Reparacion;

public interface IReparacionService {
	
	public List<Reparacion> findAll();
	
	public Page<Reparacion> findAll(Pageable pageable);
	
	public void save(Reparacion reparacion);
	
	public Reparacion findOne(Long id);

}
