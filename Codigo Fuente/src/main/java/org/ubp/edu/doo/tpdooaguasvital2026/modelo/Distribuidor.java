package org.ubp.edu.doo.tpdooaguasvital2026.modelo;

import java.util.List;

public class Distribuidor {
    private int id;
    private int cantMaxEntrega;
    private String radio;
    private String codigoZona;
    private List<Pedido> pedidos;

    public Distribuidor() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCantMaxEntrega() { return cantMaxEntrega; }
    public void setCantMaxEntrega(int cantMaxEntrega) { this.cantMaxEntrega = cantMaxEntrega; }
    public String getRadio() { return radio; }
    public void setRadio(String radio) { this.radio = radio; }
    public String getCodigoZona() { return codigoZona; }
    public void setCodigoZona(String codigoZona) { this.codigoZona = codigoZona; }
    public List<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(List<Pedido> pedidos) { this.pedidos = pedidos; }

    public void realizarCobranza() {}

    @Override
    public String toString() {
        return "Distribuidor - " + radio;
    }
}
