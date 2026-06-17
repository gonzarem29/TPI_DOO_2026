package org.ubp.edu.doo.tpdooaguasvital2026.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.ProductoDto;

public class ProductoDao implements Dao<ProductoDto> {

    private ConexionSql conexion;

    @Override
    public ProductoDto buscar(ProductoDto dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ProductoDto> listarPorCriterio(ProductoDto dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ProductoDto> listarTodos() {
        this.conexion = new ConexionSql();
        Connection con;
        Statement sentencia = null;
        ResultSet rs = null;
        List<ProductoDto> lista = new ArrayList<>();

        try {
            con = this.conexion.getConnection();
            String sql = "select p.codProducto, p.nomProducto "
                    + "from producto p "
                    + "order by p.nomProducto";
            sentencia = con.createStatement();

            rs = sentencia.executeQuery(sql);

            String codProducto, nomProducto;
            ProductoDto producto;

            while (rs.next()) {
                codProducto = rs.getString("codProducto");
                nomProducto = rs.getString("nomProducto");
                producto = new ProductoDto(codProducto, nomProducto);
                lista.add(producto);
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            try {
                rs.close();
                sentencia.close();
                this.conexion.cerrar();
            } catch (Exception ex) {
            }
        }
        return lista;
    }

    @Override
    public boolean insertar(ProductoDto dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean modificar(ProductoDto dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean borrar(ProductoDto dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
