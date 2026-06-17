package org.ubp.edu.doo.tpdooaguasvital2026.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.ClienteDto;

public class ClienteDao implements Dao<ClienteDto> {

    private ConexionSql conexion;

    @Override
    public ClienteDto buscar(ClienteDto dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ClienteDto> listarPorCriterio(ClienteDto dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ClienteDto> listarTodos() {
        this.conexion = new ConexionSql();
        Connection con;
        Statement sentencia = null;
        ResultSet rs = null;
        List<ClienteDto> lista = new ArrayList<>();

        try {
            con = this.conexion.getConnection();
            String sql = "select c.nroCliente, c.documento, c.nombre, c.apellido, "
                    + "z.codigo as zona, z.nombre as zonaNombre "
                    + "from cliente c "
                    + "left join domicilio d on c.idDomicilio = d.id "
                    + "left join barrio b on d.idBarrio = b.id "
                    + "left join zona z on b.codigoZona = z.codigo "
                    + "order by c.nombre, c.apellido";
            sentencia = con.createStatement();

            rs = sentencia.executeQuery(sql);

            int nroCliente;
            String documento, nombreCli, apellidoCli;
            ClienteDto cliente;

            while (rs.next()) {
                nroCliente = rs.getInt("nroCliente");
                documento = rs.getString("documento");
                nombreCli = rs.getString("nombre");
                apellidoCli = rs.getString("apellido");
                cliente = new ClienteDto(nroCliente, documento, nombreCli, apellidoCli);
                cliente.setZona(rs.getString("zona"));
                cliente.setZonaNombre(rs.getString("zonaNombre"));
                lista.add(cliente);
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
    public boolean insertar(ClienteDto dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean modificar(ClienteDto dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean borrar(ClienteDto dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
