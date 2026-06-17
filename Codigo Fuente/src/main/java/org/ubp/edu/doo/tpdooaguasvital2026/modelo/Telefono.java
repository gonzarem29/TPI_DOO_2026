package org.ubp.edu.doo.tpdooaguasvital2026.modelo;

public class Telefono {
    private String caracteristica;
    private String nroTelefonico;
    private String tipoTelefono;

    public Telefono() {}

    public Telefono(String caracteristica, String nroTelefonico, String tipoTelefono) {
        this.caracteristica = caracteristica;
        this.nroTelefonico = nroTelefonico;
        this.tipoTelefono = tipoTelefono;
    }

    public String getCaracteristica() { return caracteristica; }
    public void setCaracteristica(String caracteristica) { this.caracteristica = caracteristica; }
    public String getNroTelefonico() { return nroTelefonico; }
    public void setNroTelefonico(String nroTelefonico) { this.nroTelefonico = nroTelefonico; }
    public String getTipoTelefono() { return tipoTelefono; }
    public void setTipoTelefono(String tipoTelefono) { this.tipoTelefono = tipoTelefono; }

    public String getNumeroCompleto() {
        return "(" + caracteristica + ") " + nroTelefonico;
    }

    @Override
    public String toString() {
        return getNumeroCompleto();
    }
}
