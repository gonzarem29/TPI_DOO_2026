package org.ubp.edu.doo.tpdooaguasvital2026.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.PrecioDto;

public class PrecioDao {

    private ConexionSql conexion;

    public PrecioDto buscarPrecioActual(String codProducto) {
        this.conexion = new ConexionSql();
        Connection con;
        PreparedStatement sentencia = null;
        ResultSet rs = null;

        try {
            con = this.conexion.getConnection();
            String sql = "select p.precio from precio p "
                    + "where p.codProducto = ? "
                    + "order by p.fechaDesde desc limit 1";
            sentencia = con.prepareStatement(sql);
            sentencia.setString(1, codProducto);
            rs = sentencia.executeQuery();
            if (rs.next()) {
                return new PrecioDto(codProducto, rs.getDouble("precio"));
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
