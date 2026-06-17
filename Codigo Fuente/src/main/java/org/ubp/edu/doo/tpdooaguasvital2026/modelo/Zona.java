package org.ubp.edu.doo.tpdooaguasvital2026.modelo;

public class Zona {
    private String codigo;
    private String nombre;
    private java.util.List<Barrio> barrios;

    public Zona() {}

    public Zona(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public java.util.List<Barrio> getBarrios() { return barrios; }
    public void setBarrios(java.util.List<Barrio> barrios) { this.barrios = barrios; }

    @Override
    public String toString() {
        return nombre;
    }
}
