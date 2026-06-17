package org.ubp.edu.doo.tpdooaguasvital2026.modelo;

import java.util.List;

public class TipoProducto {
    private String codTipoproducto;
    private String descripcion;
    private List<Precio> precios;

    public TipoProducto() {}

    public TipoProducto(String codTipoproducto, String descripcion) {
        this.codTipoproducto = codTipoproducto;
        this.descripcion = descripcion;
    }

    public String getCodTipoproducto() { return codTipoproducto; }
    public void setCodTipoproducto(String codTipoproducto) { this.codTipoproducto = codTipoproducto; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public List<Precio> getPrecios() { return precios; }
    public void setPrecios(List<Precio> precios) { this.precios = precios; }

    public double getPrecioActual() {
        if (precios != null && !precios.isEmpty()) {
            return precios.get(precios.size() - 1).getMonto();
        }
        return 0;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
