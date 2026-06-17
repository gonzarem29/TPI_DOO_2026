package org.ubp.edu.doo.tpdooaguasvital2026.dto;

public class ClienteDto {
    private int nroCliente;
    private String documento;
    private String nombre;
    private String apellido;
    private String razonSocial;
    private String direccionCompleta;
    private String zona;
    private String zonaNombre;

    public ClienteDto() {}

    public ClienteDto(int nroCliente, String documento, String nombre, String apellido) {
        this.nroCliente = nroCliente;
        this.documento = documento;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public int getNroCliente() { return nroCliente; }
    public void setNroCliente(int nroCliente) { this.nroCliente = nroCliente; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public String getDireccionCompleta() { return direccionCompleta; }
    public void setDireccionCompleta(String direccionCompleta) { this.direccionCompleta = direccionCompleta; }
    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }
    public String getZonaNombre() { return zonaNombre; }
    public void setZonaNombre(String zonaNombre) { this.zonaNombre = zonaNombre; }
}
