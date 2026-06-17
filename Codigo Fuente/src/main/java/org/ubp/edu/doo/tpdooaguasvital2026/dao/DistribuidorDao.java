package org.ubp.edu.doo.tpdooaguasvital2026.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.DistribuidorDto;

public class DistribuidorDao implements Dao<DistribuidorDto> {

    private ConexionSql conexion;

    @Override
    public DistribuidorDto buscar(DistribuidorDto dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<DistribuidorDto> listarPorCriterio(DistribuidorDto dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<DistribuidorDto> listarTodos() {
        this.conexion = new ConexionSql();
        Connection con;
        Statement sentencia = null;
        ResultSet rs = null;
        List<DistribuidorDto> lista = new ArrayList<>();

        try {
            con = this.conexion.getConnection();
            String sql = "select d.id, d.cantMaxEntrega, d.radio, d.codigoZona "
                    + "from distribuidor d "
                    + "order by d.radio";
            sentencia = con.createStatement();

            rs = sentencia.executeQuery(sql);

            int id, cantMaxEntrega;
            String radio;
            DistribuidorDto distribuidor;

            while (rs.next()) {
                id = rs.getInt("id");
                cantMaxEntrega = rs.getInt("cantMaxEntrega");
                radio = rs.getString("radio");
                distribuidor = new DistribuidorDto();
                distribuidor.setId(id);
                distribuidor.setCantMaxEntrega(cantMaxEntrega);
                distribuidor.setRadio(radio);
                distribuidor.setCodigoZona(rs.getString("codigoZona"));
                lista.add(distribuidor);
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
    public boolean insertar(DistribuidorDto dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean modificar(DistribuidorDto dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean borrar(DistribuidorDto dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public DistribuidorDto buscarPorZona(String zona) {
        this.conexion = new ConexionSql();
        Connection con;
        PreparedStatement sentencia = null;
        ResultSet rs = null;

        try {
            con = this.conexion.getConnection();
            String sql = "select d.id, d.cantMaxEntrega, d.radio, d.codigoZona "
                    + "from distribuidor d "
                    + "where d.codigoZona = ? limit 1";
            sentencia = con.prepareStatement(sql);
            sentencia.setString(1, zona);
            rs = sentencia.executeQuery();
            if (rs.next()) {
                DistribuidorDto dto = new DistribuidorDto();
                dto.setId(rs.getInt("id"));
                dto.setCantMaxEntrega(rs.getInt("cantMaxEntrega"));
                dto.setRadio(rs.getString("radio"));
                dto.setCodigoZona(rs.getString("codigoZona"));
                return dto;
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (sentencia != null) sentencia.close();
                this.conexion.cerrar();
            } catch (Exception ex) {
            }
        }
        return null;
    }
}
