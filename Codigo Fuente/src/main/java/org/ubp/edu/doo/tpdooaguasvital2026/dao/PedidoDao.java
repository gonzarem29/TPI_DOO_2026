package org.ubp.edu.doo.tpdooaguasvital2026.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.ClienteDto;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.DetallePedidoDto;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.OperadorDto;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.PedidoDto;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.ProductoDto;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.ZonaDto;

public class PedidoDao implements Dao<PedidoDto> {

    private ConexionSql conexion;

    @Override
    public PedidoDto buscar(PedidoDto dto) {
        this.conexion = new ConexionSql();
        Connection con;
        PreparedStatement sentencia = null;
        ResultSet rs = null;

        try {
            con = this.conexion.getConnection();
            String sql = "select p.nroPedido, p.fecha, p.fechaEntrega, p.estado, p.codigoZona, p.legajoOperador, "
                    + "c.nroCliente, c.documento, c.nombre, c.apellido, "
                    + "d.id as idDet, d.cantidad, d.precio, "
                    + "pr.codProducto, pr.nomProducto "
                    + "from pedido p, cliente c, detallePedido d, producto pr "
                    + "where p.nroCliente = c.nroCliente "
                    + "and d.nroPedido = p.nroPedido "
                    + "and pr.codProducto = d.codProducto "
                    + "and p.nroPedido = ?";
            sentencia = con.prepareStatement(sql);
            sentencia.setInt(1, dto.getNroPedido());

            rs = sentencia.executeQuery();

            int nroCliente;
            String documento, nombreCli, apellidoCli;
            String codProducto, nomProducto;
            double precio;
            int cantidad;
            List<DetallePedidoDto> listaDet = new ArrayList<>();
            boolean first = true;

            while (rs.next()) {
                if (first) {
                    nroCliente = rs.getInt("nroCliente");
                    documento = rs.getString("documento");
                    nombreCli = rs.getString("nombre");
                    apellidoCli = rs.getString("apellido");
                    dto.setCliente(new ClienteDto(nroCliente, documento, nombreCli, apellidoCli));
                    String fechaStr = rs.getString("fecha");
                    dto.setFecha(fechaStr != null ? java.sql.Timestamp.valueOf(fechaStr) : null);
                    String fechaEntStr = rs.getString("fechaEntrega");
                    dto.setFechaEntrega(fechaEntStr != null ? java.sql.Timestamp.valueOf(fechaEntStr) : null);
                    dto.setEstado(rs.getString("estado"));
                    String codZona = rs.getString("codigoZona");
                    if (codZona != null) {
                        ZonaDto zonaDto = new ZonaDto();
                        zonaDto.setCodigo(codZona);
                        dto.setZona(zonaDto);
                    }
                    String legajoOp = rs.getString("legajoOperador");
                    if (legajoOp != null) {
                        OperadorDto opDto = new OperadorDto();
                        opDto.setLegajo(legajoOp);
                        dto.setOperador(opDto);
                    }
                    first = false;
                }
                codProducto = rs.getString("codProducto");
                nomProducto = rs.getString("nomProducto");
                precio = rs.getDouble("precio");
                cantidad = rs.getInt("cantidad");
                listaDet.add(new DetallePedidoDto(
                        new ProductoDto(codProducto, nomProducto), precio, cantidad));
            }
            dto.setDetalles(listaDet);
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
        return dto;
    }

    @Override
    public List<PedidoDto> listarPorCriterio(PedidoDto dto) {
        this.conexion = new ConexionSql();
        Connection con;
        PreparedStatement sentencia = null;
        ResultSet rs = null;
        List<PedidoDto> lista = new ArrayList<>();

        try {
            con = this.conexion.getConnection();
            String sql = "select p.nroPedido, p.fecha, p.estado, p.codigoZona, p.legajoOperador, "
                    + "c.nroCliente, c.documento, c.nombre, c.apellido "
                    + "from pedido p, cliente c "
                    + "where p.nroCliente = c.nroCliente "
                    + "and p.nroPedido = ? "
                    + "order by p.nroPedido";
            sentencia = con.prepareStatement(sql);
            sentencia.setInt(1, dto.getNroPedido());

            rs = sentencia.executeQuery();

            int nroPedido, nroCliente;
            Date fecha;
            String documento, nombreCli, apellidoCli, estado;
            PedidoDto pedido;

            while (rs.next()) {
                nroPedido = rs.getInt("nroPedido");
                String fechaStr = rs.getString("fecha");
                fecha = fechaStr != null ? java.sql.Timestamp.valueOf(fechaStr) : null;
                estado = rs.getString("estado");
                nroCliente = rs.getInt("nroCliente");
                documento = rs.getString("documento");
                nombreCli = rs.getString("nombre");
                apellidoCli = rs.getString("apellido");
                pedido = new PedidoDto(nroPedido, fecha,
                        new ClienteDto(nroCliente, documento, nombreCli, apellidoCli));
                pedido.setEstado(estado);
                String                 codZona = rs.getString("codigoZona");
                if (codZona != null) {
                    ZonaDto zDto = new ZonaDto();
                    zDto.setCodigo(codZona);
                    pedido.setZona(zDto);
                }
                String legOp = rs.getString("legajoOperador");
                if (legOp != null) {
                    OperadorDto opDto = new OperadorDto();
                    opDto.setLegajo(legOp);
                    pedido.setOperador(opDto);
                }
                lista.add(pedido);
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
    public List<PedidoDto> listarTodos() {
        this.conexion = new ConexionSql();
        Connection con;
        Statement sentencia = null;
        ResultSet rs = null;
        List<PedidoDto> lista = new ArrayList<>();

        try {
            con = this.conexion.getConnection();
            String sql = "select p.nroPedido, p.fecha, p.estado, p.codigoZona, p.legajoOperador, "
                    + "c.nroCliente, c.documento, c.nombre, c.apellido "
                    + "from pedido p, cliente c "
                    + "where p.nroCliente = c.nroCliente "
                    + "order by p.nroPedido";
            sentencia = con.createStatement();

            rs = sentencia.executeQuery(sql);

            int nroPedido, nroCliente;
            Date fecha;
            String documento, nombreCli, apellidoCli, estado, codZona;
            PedidoDto pedido;

            while (rs.next()) {
                nroPedido = rs.getInt("nroPedido");
                String fechaStr = rs.getString("fecha");
                fecha = fechaStr != null ? java.sql.Timestamp.valueOf(fechaStr) : null;
                estado = rs.getString("estado");
                nroCliente = rs.getInt("nroCliente");
                documento = rs.getString("documento");
                nombreCli = rs.getString("nombre");
                apellidoCli = rs.getString("apellido");
                pedido = new PedidoDto(nroPedido, fecha,
                        new ClienteDto(nroCliente, documento, nombreCli, apellidoCli));
                pedido.setEstado(estado);
                codZona = rs.getString("codigoZona");
                if (codZona != null) {
                    ZonaDto zDto = new ZonaDto();
                    zDto.setCodigo(codZona);
                    pedido.setZona(zDto);
                }
                String legOp = rs.getString("legajoOperador");
                if (legOp != null) {
                    OperadorDto opDto = new OperadorDto();
                    opDto.setLegajo(legOp);
                    pedido.setOperador(opDto);
                }
                lista.add(pedido);
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
    public boolean insertar(PedidoDto dto) {
        this.conexion = new ConexionSql();
        Connection con = null;
        PreparedStatement sentencia = null;

        try {
            con = this.conexion.getConnection();
            con.setAutoCommit(false);
            String sql = "insert into pedido (fecha, nroCliente, legajoOperador, codigoZona, estado) "
                    + "values(?, "
                    + "(select c.nroCliente from cliente c where c.documento like ?), "
                    + "?, ?, 'PENDIENTE')";
            sentencia = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            sentencia.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dto.getFecha()));
            sentencia.setString(2, dto.getCliente().getDocumento());
            sentencia.setString(3, dto.getOperador() != null ? dto.getOperador().getLegajo() : "1");
            sentencia.setString(4, dto.getZona() != null ? dto.getZona().getCodigo() : null);

            int resultado = sentencia.executeUpdate();

            if (resultado <= 0) {
                con.rollback();
                return false;
            }

            ResultSet rs = sentencia.getGeneratedKeys();
            int nroPedidoUltimo = 0;
            while (rs.next()) {
                nroPedidoUltimo = rs.getInt(1);
            }
            dto.setNroPedido(nroPedidoUltimo);

            for (DetallePedidoDto detalle : dto.getDetalles()) {
                sql = "insert into detallePedido (nroPedido, codProducto, precio, cantidad) "
                        + "values(?,?,?,?)";
                sentencia = con.prepareStatement(sql);
                sentencia.setInt(1, nroPedidoUltimo);
                sentencia.setString(2, detalle.getProducto().getCodProducto());
                sentencia.setDouble(3, detalle.getPrecio());
                sentencia.setInt(4, detalle.getCantidad());
                resultado = sentencia.executeUpdate();
                if (resultado <= 0) {
                    con.rollback();
                    return false;
                }
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                }
            }
            return false;
        } finally {
            try {
                sentencia.close();
                this.conexion.cerrar();
            } catch (Exception ex) {
                System.err.println(ex);
            }
        }
    }

    @Override
    public boolean modificar(PedidoDto dto) {
        this.conexion = new ConexionSql();
        Connection con = null;
        PreparedStatement sentencia = null;

        try {
            con = this.conexion.getConnection();
            con.setAutoCommit(false);
            String sql = "update pedido set fecha=?, "
                    + "estado=?, codigoZona=?, fechaEntrega=?, legajoOperador=?, "
                    + "nroCliente=(select c.nroCliente from cliente c where c.documento like ?) "
                    + "where nroPedido=?";
            sentencia = con.prepareStatement(sql);
            sentencia.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dto.getFecha()));
            sentencia.setString(2, dto.getEstado());
            sentencia.setString(3, dto.getZona() != null ? dto.getZona().getCodigo() : null);
            sentencia.setString(4, dto.getFechaEntrega() != null
                    ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dto.getFechaEntrega()) : null);
            sentencia.setString(5, dto.getOperador() != null ? dto.getOperador().getLegajo() : null);
            sentencia.setString(6, dto.getCliente().getDocumento());
            sentencia.setInt(7, dto.getNroPedido());

            int resultado = sentencia.executeUpdate();

            if (resultado <= 0) {
                con.rollback();
                return false;
            }

            sql = "delete from detallePedido where nroPedido=?";
            sentencia = con.prepareStatement(sql);
            sentencia.setInt(1, dto.getNroPedido());
            sentencia.executeUpdate();

            for (DetallePedidoDto detalle : dto.getDetalles()) {
                sql = "insert into detallePedido (nroPedido, codProducto, precio, cantidad) "
                        + "values(?,?,?,?)";
                sentencia = con.prepareStatement(sql);
                sentencia.setInt(1, dto.getNroPedido());
                sentencia.setString(2, detalle.getProducto().getCodProducto());
                sentencia.setDouble(3, detalle.getPrecio());
                sentencia.setInt(4, detalle.getCantidad());
                resultado = sentencia.executeUpdate();
                if (resultado <= 0) {
                    con.rollback();
                    return false;
                }
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                }
            }
            return false;
        } finally {
            try {
                sentencia.close();
                this.conexion.cerrar();
            } catch (Exception ex) {
                System.err.println(ex);
            }
        }
    }

    @Override
    public boolean borrar(PedidoDto dto) {
        this.conexion = new ConexionSql();
        Connection con = null;
        PreparedStatement sentencia = null;

        try {
            con = this.conexion.getConnection();
            con.setAutoCommit(false);
            String sql = "delete from detallePedido where nroPedido=?";
            sentencia = con.prepareStatement(sql);
            sentencia.setInt(1, dto.getNroPedido());
            int resultado = sentencia.executeUpdate();

            sql = "delete from pedido where nroPedido=?";
            sentencia = con.prepareStatement(sql);
            sentencia.setInt(1, dto.getNroPedido());
            resultado = sentencia.executeUpdate();
            if (resultado <= 0) {
                con.rollback();
                return false;
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                }
            }
            return false;
        } finally {
            try {
                sentencia.close();
                this.conexion.cerrar();
            } catch (Exception ex) {
                System.err.println(ex);
            }
        }
    }

}
