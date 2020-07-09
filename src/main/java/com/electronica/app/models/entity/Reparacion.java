package com.electronica.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "reparaciones")
public class Reparacion implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Cliente cliente;

	@NotEmpty
	private String descripcion;

	private String observacion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_recepcion")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date fechaRecepcion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_entrega")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date fechaEntrega;

	private Integer aCuenta;

	@NotNull
	private Integer total;

	public Reparacion() {
	}

	@PrePersist
	public void prePersist() {
		fechaRecepcion = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Date getFechaRecepcion() {
		return fechaRecepcion;
	}

	public void setFechaRecepcion(Date fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}

	public Date getFechaEntrega() {
		return fechaEntrega;
	}

	public void setFechaEntrega(Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

	public Integer getaCuenta() {
		return aCuenta;
	}

	public void setaCuenta(Integer aCuenta) {
		this.aCuenta = aCuenta;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
	
	public Integer getPagar() {
		int porPagar = 0;
		if (this.aCuenta != null) {
			porPagar = this.total - this.aCuenta;
		} else {
			porPagar = this.total;
		}
		return porPagar;
	}

	public String getTiempo() {
		String tiempo = "";

		int dias = 0;
		int horas = 0;
		int minutos = 0;

		try {

			int diferencia = (int) ((this.fechaEntrega.getTime() - this.fechaRecepcion.getTime()) / 1000);

			if (diferencia >= 86400) {
				dias = (int) Math.floor(diferencia / 86400);
				diferencia = diferencia - (dias * 86400);
			}
			if (diferencia >= 3600) {
				horas = (int) Math.floor(diferencia / 3600);
				diferencia = diferencia - (horas * 3600);
			}
			if (diferencia >= 60) {
				minutos = (int) Math.floor(diferencia / 60);
				diferencia = diferencia - (minutos * 60);
			}

			tiempo = dias + " Dias " + horas + " Hr " + minutos + " Min";

		} catch (Exception ex) {
			tiempo = "Aun no esta terminado";
		}

		return tiempo;

	}

}
