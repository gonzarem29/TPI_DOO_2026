package org.ubp.edu.doo.tpdooaguasvital2026.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.ZonaDto;

public class ZonaDao implements Dao<ZonaDto> {

    private ConexionSql conexion;

    @Override
    public ZonaDto buscar(ZonaDto dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ZonaDto> listarPorCriterio(ZonaDto dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ZonaDto> listarTodos() {
        this.conexion = new ConexionSql();
        Connection con;
        Statement sentencia = null;
        ResultSet rs = null;
        List<ZonaDto> lista = new ArrayList<>();

        try {
            con = this.conexion.getConnection();
            String sql = "select z.codigo, z.nombre "
                    + "from zona z "
                    + "order by z.nombre";
            sentencia = con.createStatement();

            rs = sentencia.executeQuery(sql);

            String codigo, nombre;
            ZonaDto zona;

            while (rs.next()) {
                codigo = rs.getString("codigo");
                nombre = rs.getString("nombre");
                zona = new ZonaDto();
                zona.setCodigo(codigo);
                zona.setNombre(nombre);
                lista.add(zona);
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
    public boolean insertar(ZonaDto dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean modificar(ZonaDto dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean borrar(ZonaDto dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
