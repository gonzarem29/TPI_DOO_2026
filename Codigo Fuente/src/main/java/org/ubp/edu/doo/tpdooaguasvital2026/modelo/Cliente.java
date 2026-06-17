package org.ubp.edu.doo.tpdooaguasvital2026.modelo;

import org.ubp.edu.doo.tpdooaguasvital2026.factories.FabricaDao;
import java.util.Arrays;
import java.util.List;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.ClienteDto;

public class Cliente extends Modelo {
    private int nroCliente;
    private String documento;
    private String nombre;
    private String apellido;
    private String razonSocial;
    private Domicilio domicilio;
    private Telefono telefono;
    private String zona;
    private String zonaNombre;

    public Cliente() {
        this.dao = FabricaDao.fabricar("ClienteDao");
    }

    public List<Cliente> listarTodos() {
        List<ClienteDto> dtos = this.dao.listarTodos();
        return Arrays.asList(this.mapper.map(dtos, Cliente[].class));
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
    public Domicilio getDomicilio() { return domicilio; }
    public void setDomicilio(Domicilio domicilio) { this.domicilio = domicilio; }
    public Telefono getTelefono() { return telefono; }
    public void setTelefono(Telefono telefono) { this.telefono = telefono; }
    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }
    public String getZonaNombre() { return zonaNombre; }
    public void setZonaNombre(String zonaNombre) { this.zonaNombre = zonaNombre; }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public boolean tieneDeuda() {
        return false;
    }

    public String getDireccionCompleta() {
        return domicilio != null ? domicilio.getDireccionCompleta() : "";
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " (DNI: " + documento + ")";
    }
}
