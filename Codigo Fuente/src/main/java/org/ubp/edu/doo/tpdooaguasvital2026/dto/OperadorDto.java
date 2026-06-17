package org.ubp.edu.doo.tpdooaguasvital2026.dto;

public class OperadorDto {
    private String legajo;
    private String nombre;
    private String apellido;

    public OperadorDto() {}

    public OperadorDto(String legajo, String nombre, String apellido) {
        this.legajo = legajo;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getLegajo() { return legajo; }
    public void setLegajo(String legajo) { this.legajo = legajo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
}
