package org.ubp.edu.doo.tpdooaguasvital2026.dto;

import java.util.Date;

public class FacturaDto {
    private int nroFactura;
    private Date fecha;
    private ClienteDto cliente;
    private String detalleFactura;

    public FacturaDto() {}

    public int getNroFactura() { return nroFactura; }
    public void setNroFactura(int nroFactura) { this.nroFactura = nroFactura; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public ClienteDto getCliente() { return cliente; }
    public void setCliente(ClienteDto cliente) { this.cliente = cliente; }
    public String getDetalleFactura() { return detalleFactura; }
    public void setDetalleFactura(String detalleFactura) { this.detalleFactura = detalleFactura; }
}
