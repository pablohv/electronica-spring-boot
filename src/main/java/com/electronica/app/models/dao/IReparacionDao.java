package com.electronica.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.electronica.app.models.entity.Reparacion;

public interface IReparacionDao extends PagingAndSortingRepository<Reparacion, Long> {

}
