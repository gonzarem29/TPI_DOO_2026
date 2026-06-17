package org.ubp.edu.doo.tpdooaguasvital2026.modelo;

import java.util.Date;

public class Precio {
    private double monto;
    private Date fechaVigencia;
    private TipoProducto tipoProducto;

    public Precio() {}

    public Precio(double monto, Date fechaVigencia, TipoProducto tipoProducto) {
        this.monto = monto;
        this.fechaVigencia = fechaVigencia;
        this.tipoProducto = tipoProducto;
    }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public Date getFechaVigencia() { return fechaVigencia; }
    public void setFechaVigencia(Date fechaVigencia) { this.fechaVigencia = fechaVigencia; }
    public TipoProducto getTipoProducto() { return tipoProducto; }
    public void setTipoProducto(TipoProducto tipoProducto) { this.tipoProducto = tipoProducto; }

    public double getMontoActual() {
        return monto;
    }
}
