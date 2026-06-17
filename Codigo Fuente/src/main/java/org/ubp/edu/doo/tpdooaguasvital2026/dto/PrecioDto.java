package org.ubp.edu.doo.tpdooaguasvital2026.dto;

public class PrecioDto {
    private String codProducto;
    private double monto;

    public PrecioDto() {}

    public PrecioDto(String codProducto, double monto) {
        this.codProducto = codProducto;
        this.monto = monto;
    }

    public String getCodProducto() { return codProducto; }
    public void setCodProducto(String codProducto) { this.codProducto = codProducto; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
}
