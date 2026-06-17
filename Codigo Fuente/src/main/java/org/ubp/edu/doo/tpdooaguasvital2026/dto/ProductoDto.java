package org.ubp.edu.doo.tpdooaguasvital2026.dto;

public class ProductoDto {
    private String codProducto;
    private String nomProducto;

    public ProductoDto() {}

    public ProductoDto(String codProducto, String nomProducto) {
        this.codProducto = codProducto;
        this.nomProducto = nomProducto;
    }

    public String getCodProducto() { return codProducto; }
    public void setCodProducto(String codProducto) { this.codProducto = codProducto; }
    public String getNomProducto() { return nomProducto; }
    public void setNomProducto(String nomProducto) { this.nomProducto = nomProducto; }
}
