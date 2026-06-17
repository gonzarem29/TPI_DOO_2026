package org.ubp.edu.doo.tpdooaguasvital2026.dto;

import java.util.Date;
import java.util.List;

public class PedidoDto {
    private int nroPedido;
    private Date fecha;
    private Date fechaEntrega;
    private String estado;
    private ClienteDto cliente;
    private ZonaDto zona;
    private OperadorDto operador;
    private List<DetallePedidoDto> detallePedido;

    public PedidoDto() {}

    public PedidoDto(int nroPedido, Date fecha, ClienteDto cliente) {
        this.nroPedido = nroPedido;
        this.fecha = fecha;
        this.cliente = cliente;
    }

    public int getNroPedido() { return nroPedido; }
    public void setNroPedido(int nroPedido) { this.nroPedido = nroPedido; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public Date getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(Date fechaEntrega) { this.fechaEntrega = fechaEntrega; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public ClienteDto getCliente() { return cliente; }
    public void setCliente(ClienteDto cliente) { this.cliente = cliente; }
    public ZonaDto getZona() { return zona; }
    public void setZona(ZonaDto zona) { this.zona = zona; }
    public OperadorDto getOperador() { return operador; }
    public void setOperador(OperadorDto operador) { this.operador = operador; }
    public List<DetallePedidoDto> getDetalles() { return detallePedido; }
    public void setDetalles(List<DetallePedidoDto> detallePedido) { this.detallePedido = detallePedido; }
}
