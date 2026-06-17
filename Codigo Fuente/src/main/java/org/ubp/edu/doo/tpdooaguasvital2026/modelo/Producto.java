package org.ubp.edu.doo.tpdooaguasvital2026.modelo;

import java.util.Arrays;
import java.util.List;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.ProductoDto;
import org.ubp.edu.doo.tpdooaguasvital2026.factories.FabricaDao;

public class Producto extends Modelo {
    private String codProducto;
    private String nomProducto;

    public Producto() {
        this.dao = FabricaDao.fabricar("ProductoDao");
    }

    public Producto(String codProducto, String nomProducto) {
        this.dao = FabricaDao.fabricar("ProductoDao");
        this.codProducto = codProducto;
        this.nomProducto = nomProducto;
    }

    public List<Producto> listarTodos() {
        List<ProductoDto> dtos = this.dao.listarTodos();
        return Arrays.asList(this.mapper.map(dtos, Producto[].class));
    }

    public String getCodProducto() { return codProducto; }
    public void setCodProducto(String codProducto) { this.codProducto = codProducto; }
    public String getNomProducto() { return nomProducto; }
    public void setNomProducto(String nomProducto) { this.nomProducto = nomProducto; }

    @Override
    public String toString() {
        return nomProducto;
    }
}
