package org.ubp.edu.doo.tpdooaguasvital2026.modelo;

import java.util.Date;

public class Stock {
    private int cantidad;
    private Producto producto;
    private Date fechaActualizacion;

    public Stock() {}

    public Stock(int cantidad, Producto producto, Date fechaActualizacion) {
        this.cantidad = cantidad;
        this.producto = producto;
        this.fechaActualizacion = fechaActualizacion;
    }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    public Date getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(Date fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public boolean verificarDisponibilidad() {
        return cantidad > 0;
    }

    public void actualizarCantidad(int nuevaCantidad) {
        this.cantidad = nuevaCantidad;
        this.fechaActualizacion = new Date();
    }
}
