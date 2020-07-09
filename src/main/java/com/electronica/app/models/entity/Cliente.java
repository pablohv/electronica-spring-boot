package com.electronica.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String nombre;
	
	@NotEmpty
	@Pattern(regexp = "[0-9]*")
	private String telefono;

	@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Factura> facturas;
	
	@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Reparacion> reparaciones;
	
	public Cliente() {
		facturas = new ArrayList<Factura>();
		reparaciones = new ArrayList<Reparacion>();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public List<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}
	
	public void addFactura(Factura factura) {
		this.facturas.add(factura);
	}

	public List<Reparacion> getReparaciones() {
		return reparaciones;
	}

	public void setReparaciones(List<Reparacion> reparaciones) {
		this.reparaciones = reparaciones;
	}
	
	public void addReparacion(Reparacion reparacion) {
		this.reparaciones.add(reparacion);
	}

}
