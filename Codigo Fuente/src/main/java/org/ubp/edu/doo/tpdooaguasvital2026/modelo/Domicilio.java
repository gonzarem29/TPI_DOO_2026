package org.ubp.edu.doo.tpdooaguasvital2026.modelo;

public class Domicilio {
    private String direccion;
    private String barrio;
    private String zona;

    public Domicilio() {}

    public Domicilio(String direccion, String barrio, String zona) {
        this.direccion = direccion;
        this.barrio = barrio;
        this.zona = zona;
    }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getBarrio() { return barrio; }
    public void setBarrio(String barrio) { this.barrio = barrio; }
    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }

    public String getDireccionCompleta() {
        return direccion + ", " + barrio + " (" + zona + ")";
    }
}
