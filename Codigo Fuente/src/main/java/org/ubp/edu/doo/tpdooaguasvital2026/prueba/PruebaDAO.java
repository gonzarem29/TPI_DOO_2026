package org.ubp.edu.doo.tpdooaguasvital2026.prueba;

import java.util.Date;
import java.util.List;
import org.ubp.edu.doo.tpdooaguasvital2026.dao.*;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.*;
import org.ubp.edu.doo.tpdooaguasvital2026.util.InicializadorBD;

public class PruebaDAO {

    public static void main(String[] args) {
        System.out.println("=== INICIALIZANDO BASE DE DATOS ===");
        InicializadorBD.inicializar();

        System.out.println("\n=== PRUEBA DE DAOs ===\n");

        // 1. ClienteDao - listarTodos
        System.out.println("--- ClienteDao.listarTodos() ---");
        ClienteDao clienteDao = new ClienteDao();
        List<ClienteDto> clientes = clienteDao.listarTodos();
        for (ClienteDto c : clientes) {
            System.out.println("  Cliente: " + c.getNroCliente() + " - " + c.getNombre() + " " + c.getApellido() + " (Doc: " + c.getDocumento() + ")");
        }

        // 2. ProductoDao - listarTodos
        System.out.println("\n--- ProductoDao.listarTodos() ---");
        ProductoDao productoDao = new ProductoDao();
        List<ProductoDto> productos = productoDao.listarTodos();
        for (ProductoDto p : productos) {
            System.out.println("  Producto: " + p.getCodProducto() + " - " + p.getNomProducto());
        }

        // 3. ZonaDao - listarTodos
        System.out.println("\n--- ZonaDao.listarTodos() ---");
        ZonaDao zonaDao = new ZonaDao();
        List<ZonaDto> zonas = zonaDao.listarTodos();
        for (ZonaDto z : zonas) {
            System.out.println("  Zona: " + z.getCodigo() + " - " + z.getNombre());
        }

        // 4. DistribuidorDao - listarTodos
        System.out.println("\n--- DistribuidorDao.listarTodos() ---");
        DistribuidorDao distribuidorDao = new DistribuidorDao();
        List<DistribuidorDto> distribuidores = distribuidorDao.listarTodos();
        for (DistribuidorDto d : distribuidores) {
            System.out.println("  Distribuidor: " + d.getId() + " - " + d.getRadio() + " (max: " + d.getCantMaxEntrega() + ")");
        }

        // 5. PedidoDao - listarTodos
        System.out.println("\n--- PedidoDao.listarTodos() ---");
        PedidoDao pedidoDao = new PedidoDao();
        List<PedidoDto> pedidos = pedidoDao.listarTodos();
        for (PedidoDto p : pedidos) {
            System.out.println("  Pedido Nro: " + p.getNroPedido() + " - Fecha: " + p.getFecha() + " - Estado: " + p.getEstado()
                    + " - Cliente: " + p.getCliente().getNombre() + " " + p.getCliente().getApellido());
        }

        // 6. PedidoDao - buscar con detalles
        System.out.println("\n--- PedidoDao.buscar() ---");
        PedidoDto criterio = new PedidoDto();
        criterio.setNroPedido(1);
        PedidoDto pedido = pedidoDao.buscar(criterio);
        if (pedido.getDetalles() != null) {
            System.out.println("  Pedido Nro: " + pedido.getNroPedido() + " - Cliente: " + pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido() + " - Estado: " + pedido.getEstado());
            System.out.println("  Detalles:");
            for (DetallePedidoDto d : pedido.getDetalles()) {
                System.out.println("    - " + d.getProducto().getNomProducto() + " x " + d.getCantidad() + " = $" + (d.getPrecio() * d.getCantidad()));
            }
        }

        // 7. PedidoDao - insertar (UC-4)
        System.out.println("\n--- PedidoDao.insertar() (UC-4) ---");
        PedidoDto nuevoPedido = new PedidoDto();
        nuevoPedido.setFecha(new Date());
        nuevoPedido.setCliente(clientes.get(0));
        nuevoPedido.setDetalles(List.of(
                new DetallePedidoDto(productos.get(0), 150.0, 3),
                new DetallePedidoDto(productos.get(1), 250.0, 2)
        ));
        boolean insertado = pedidoDao.insertar(nuevoPedido);
        System.out.println("  Resultado insertar: " + (insertado ? "EXITO" : "FALLO"));

        // Verificar que se insertó
        List<PedidoDto> pedidosActualizados = pedidoDao.listarTodos();
        System.out.println("  Total pedidos despues de insertar: " + pedidosActualizados.size());

        System.out.println("\n=== TODAS LAS PRUEBAS FINALIZADAS ===");
    }
}
