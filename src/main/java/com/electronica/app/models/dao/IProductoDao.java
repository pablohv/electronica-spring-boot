package com.electronica.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.electronica.app.models.entity.Producto;

public interface IProductoDao extends PagingAndSortingRepository<Producto, Long> {
	
	@Query("select p from Producto p where p.nombre like %?1%")
	public List<Producto> findByNombre(String term);

	public List<Producto> findByNombreLikeIgnoreCase(String term);
	
	@Query(value = "select * from productos order by cantidad asc LIMIT 4", nativeQuery = true)
	public List<Producto> findByCantidad();
	
	@Query(value = "SELECT factura_items.producto_id, productos.nombre, sum(factura_items.cantidad) as cantidad FROM factura_items inner join productos on factura_items.producto_id=productos.id group by producto_id order by cantidad desc LIMIT 4", nativeQuery = true)
	public String[] findByMasVendido();
 
}
