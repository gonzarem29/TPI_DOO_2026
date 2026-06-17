package org.ubp.edu.doo.tpdooaguasvital2026.modelo;

import java.util.Date;

public class Factura {
    private int nroFactura;
    private Date fecha;
    private Cliente cliente;
    private String detalleFactura;
    private Pedido pedido;

    public Factura() {}

    public Factura(int nroFactura, Date fecha, Cliente cliente, Pedido pedido) {
        this.nroFactura = nroFactura;
        this.fecha = fecha;
        this.cliente = cliente;
        this.pedido = pedido;
    }

    public int getNroFactura() { return nroFactura; }
    public void setNroFactura(int nroFactura) { this.nroFactura = nroFactura; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public String getDetalleFactura() { return detalleFactura; }
    public void setDetalleFactura(String detalleFactura) { this.detalleFactura = detalleFactura; }
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public double calcularTotal() {
        return pedido != null ? pedido.calcularTotal() : 0;
    }

    public void imprimirFactura() {}
}
