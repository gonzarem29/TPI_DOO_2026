package org.ubp.edu.doo.tpdooaguasvital2026.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.OperadorDto;

public class EmpleadoDao {

    private ConexionSql conexion;

    public List<OperadorDto> listarOperadores() {
        this.conexion = new ConexionSql();
        Connection con;
        Statement sentencia = null;
        ResultSet rs = null;
        List<OperadorDto> lista = new ArrayList<>();

        try {
            con = this.conexion.getConnection();
            String sql = "select e.legajo, e.nombre, e.apellido "
                    + "from empleado e "
                    + "order by e.nombre, e.apellido";
            sentencia = con.createStatement();
            rs = sentencia.executeQuery(sql);
            while (rs.next()) {
                lista.add(new OperadorDto(
                        rs.getString("legajo"),
                        rs.getString("nombre"),
                        rs.getString("apellido")));
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
        return lista;
    }
}
