package com.electronica.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.electronica.app.models.entity.Compra;

public interface ICompraDao extends PagingAndSortingRepository<Compra, Long> {
	
	@Query("select c from Compra c join fetch c.items l join fetch l.producto where c.id=?1")
	public Compra fetchByIdWhithItemCompraWithProducto(Long id); 

}
