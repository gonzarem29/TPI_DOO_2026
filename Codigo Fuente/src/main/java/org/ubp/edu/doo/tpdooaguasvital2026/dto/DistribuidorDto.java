package org.ubp.edu.doo.tpdooaguasvital2026.dto;

public class DistribuidorDto {
    private int id;
    private int cantMaxEntrega;
    private String radio;
    private String codigoZona;

    public DistribuidorDto() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCantMaxEntrega() { return cantMaxEntrega; }
    public void setCantMaxEntrega(int cantMaxEntrega) { this.cantMaxEntrega = cantMaxEntrega; }
    public String getRadio() { return radio; }
    public void setRadio(String radio) { this.radio = radio; }
    public String getCodigoZona() { return codigoZona; }
    public void setCodigoZona(String codigoZona) { this.codigoZona = codigoZona; }
}
