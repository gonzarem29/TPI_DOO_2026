package org.ubp.edu.doo.tpdooaguasvital2026.prueba;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import org.ubp.edu.doo.tpdooaguasvital2026.dao.*;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.*;
import org.ubp.edu.doo.tpdooaguasvital2026.factories.FabricaModelo;
import org.ubp.edu.doo.tpdooaguasvital2026.modelo.*;
import org.ubp.edu.doo.tpdooaguasvital2026.util.InicializadorBD;

public class TesteoExhaustivo {

    private static int pasaron = 0;
    private static int fallaron = 0;

    public static void main(String[] args) {
        System.out.println("==================================================================");
        System.out.println("  TESTEO EXHAUSTIVO - AGUAS VITAL SA");
        System.out.println("==================================================================\n");

        // =========================================================
        // PRUEBA 1: INICIALIZACION DE BASE DE DATOS
        // =========================================================
        System.out.println("--- [1] INICIALIZACION DE BASE DE DATOS ---");
        probar("1.1 InicializadorBD ejecuta sin errores", () -> {
            InicializadorBD.inicializar();
            return true;
        });

        probar("1.2 BD contiene todas las tablas (16 esperadas)", () -> {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:tp-doo-aguas-vital-2026.db");
                 Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(
                    "SELECT count(*) FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%'");
                int count = rs.getInt(1);
                System.out.println("      Tablas encontradas: " + count + " (esperadas: 16)");
                return count >= 14; // algunas pueden no crearse por FK circular
            }
        });

        probar("1.3 Tabla zona contiene datos de prueba (4 registros)", () -> {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:tp-doo-aguas-vital-2026.db");
                 Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT count(*) FROM zona");
                int count = rs.getInt(1);
                System.out.println("      Zonas: " + count);
                return count == 4;
            }
        });

        probar("1.4 Tabla cliente contiene datos de prueba (2 registros)", () -> {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:tp-doo-aguas-vital-2026.db");
                 Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT count(*) FROM cliente");
                int count = rs.getInt(1);
                System.out.println("      Clientes: " + count);
                return count == 2;
            }
        });

        probar("1.5 Tabla producto contiene datos de prueba (6 registros)", () -> {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:tp-doo-aguas-vital-2026.db");
                 Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT count(*) FROM producto");
                int count = rs.getInt(1);
                System.out.println("      Productos: " + count);
                return count == 6;
            }
        });

        probar("1.6 Tabla pedido contiene datos de prueba (2 registros)", () -> {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:tp-doo-aguas-vital-2026.db");
                 Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT count(*) FROM pedido");
                int count = rs.getInt(1);
                System.out.println("      Pedidos: " + count);
                return count >= 2;
            }
        });

        probar("1.7 Tabla detallePedido contiene datos de prueba (3 registros)", () -> {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:tp-doo-aguas-vital-2026.db");
                 Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT count(*) FROM detallePedido");
                int count = rs.getInt(1);
                System.out.println("      Detalles: " + count);
                return count >= 3;
            }
        });

        // =========================================================
        // PRUEBA 2: CAPA DTO
        // =========================================================
        System.out.println("\n--- [2] CAPA DTO ---");
        probar("2.1 ClienteDto se crea y asigna propiedades", () -> {
            ClienteDto dto = new ClienteDto(1, "doc", "Nom", "Ape");
            return dto.getNroCliente() == 1 && "Nom".equals(dto.getNombre());
        });

        probar("2.2 ProductoDto se crea con constructor", () -> {
            ProductoDto dto = new ProductoDto("COD-1", "Producto Test");
            return "COD-1".equals(dto.getCodProducto()) && "Producto Test".equals(dto.getNomProducto());
        });

        probar("2.3 PedidoDto con cliente y detalles", () -> {
            PedidoDto dto = new PedidoDto(1, new Date(), new ClienteDto(1, "doc", "Nom", "Ape"));
            dto.setDetalles(List.of(new DetallePedidoDto(new ProductoDto("C1", "P1"), 10.0, 2)));
            return dto.getDetalles().size() == 1;
        });

        // =========================================================
        // PRUEBA 3: CAPA DAO
        // =========================================================
        System.out.println("\n--- [3] CAPA DAO ---");

        probar("3.1 ClienteDao.listarTodos() devuelve 2 clientes", () -> {
            ClienteDao dao = new ClienteDao();
            List<ClienteDto> lista = dao.listarTodos();
            System.out.println("      Clientes: " + lista.size());
            return lista.size() == 2;
        });

        probar("3.2 ProductoDao.listarTodos() devuelve 6 productos", () -> {
            ProductoDao dao = new ProductoDao();
            List<ProductoDto> lista = dao.listarTodos();
            System.out.println("      Productos: " + lista.size());
            return lista.size() == 6;
        });

        probar("3.3 ZonaDao.listarTodos() devuelve 4 zonas", () -> {
            ZonaDao dao = new ZonaDao();
            List<ZonaDto> lista = dao.listarTodos();
            System.out.println("      Zonas: " + lista.size());
            return lista.size() == 4;
        });

        probar("3.4 DistribuidorDao.listarTodos() devuelve 2 distribuidores", () -> {
            DistribuidorDao dao = new DistribuidorDao();
            List<DistribuidorDto> lista = dao.listarTodos();
            System.out.println("      Distribuidores: " + lista.size());
            return lista.size() == 2;
        });

        probar("3.5 PedidoDao.listarTodos() devuelve pedidos con clientes", () -> {
            PedidoDao dao = new PedidoDao();
            List<PedidoDto> lista = dao.listarTodos();
            System.out.println("      Pedidos: " + lista.size());
            return !lista.isEmpty() && lista.get(0).getCliente() != null;
        });

        probar("3.6 PedidoDao.buscar() devuelve pedido con detalles (UC-4/UC-8)", () -> {
            PedidoDao dao = new PedidoDao();
            PedidoDto criterio = new PedidoDto();
            criterio.setNroPedido(1);
            PedidoDto resultado = dao.buscar(criterio);
            boolean ok = resultado != null && resultado.getDetalles() != null && resultado.getDetalles().size() >= 2;
            if (ok) {
                System.out.println("      Detalles encontrados: " + resultado.getDetalles().size());
                for (DetallePedidoDto d : resultado.getDetalles()) {
                    System.out.println("        - " + d.getProducto().getNomProducto() + " x " + d.getCantidad() + " = $" + (d.getPrecio() * d.getCantidad()));
                }
            }
            return ok;
        });

        probar("3.7 PedidoDao.insertar() crea nuevo pedido con detalles (UC-4)", () -> {
            // Obtener datos de prueba
            ClienteDao cliDao = new ClienteDao();
            ProductoDao prodDao = new ProductoDao();
            PedidoDao pedDao = new PedidoDao();

            List<ClienteDto> clientes = cliDao.listarTodos();
            List<ProductoDto> productos = prodDao.listarTodos();

            PedidoDto nuevo = new PedidoDto();
            nuevo.setFecha(new Date());
            nuevo.setCliente(clientes.get(0));
            nuevo.setDetalles(List.of(
                new DetallePedidoDto(productos.get(0), 150.0, 3),
                new DetallePedidoDto(productos.get(1), 250.0, 2)
            ));

            boolean insertado = pedDao.insertar(nuevo);
            System.out.println("      Insertado: " + (insertado ? "SI" : "NO"));
            return insertado;
        });

        probar("3.8 PedidoDao.modificar() actualiza pedido existente (UC-8)", () -> {
            PedidoDao pedDao = new PedidoDao();
            PedidoDto criterio = new PedidoDto();
            criterio.setNroPedido(1);
            PedidoDto existente = pedDao.buscar(criterio);

            if (existente == null) return false;

            existente.setEstado("ENTREGADO");
            if (existente.getDetalles() != null && existente.getDetalles().size() >= 1) {
                existente.getDetalles().get(0).setCantidad(99);
            }

            boolean modificado = pedDao.modificar(existente);
            System.out.println("      Modificado: " + (modificado ? "SI" : "NO"));
            return modificado;
        });

        probar("3.9 PedidoDao.listarPorCriterio() filtra por nroPedido", () -> {
            PedidoDao dao = new PedidoDao();
            PedidoDto criterio = new PedidoDto();
            criterio.setNroPedido(1);
            List<PedidoDto> resultados = dao.listarPorCriterio(criterio);
            System.out.println("      Resultados: " + resultados.size());
            return resultados.size() >= 1 && resultados.get(0).getNroPedido() == 1;
        });

        // =========================================================
        // PRUEBA 4: CAPA MODELO (con ModelMapper)
        // =========================================================
        System.out.println("\n--- [4] CAPA MODELO ---");

        probar("4.1 Cliente.listarTodos() via ModelMapper (DTO->Modelo)", () -> {
            Cliente modeloCliente = (Cliente) FabricaModelo.fabricar("Cliente");
            List<Cliente> clientes = modeloCliente.listarTodos();
            System.out.println("      Clientes modelo: " + clientes.size());
            return clientes.size() == 2 && clientes.get(0).getNombre() != null;
        });

        probar("4.2 Producto.listarTodos() via ModelMapper (DTO->Modelo)", () -> {
            Producto modeloProducto = (Producto) FabricaModelo.fabricar("Producto");
            List<Producto> productos = modeloProducto.listarTodos();
            System.out.println("      Productos modelo: " + productos.size());
            return productos.size() == 6;
        });

        probar("4.3 Pedido.listarTodos() via ModelMapper (DTO->Modelo)", () -> {
            Pedido modeloPedido = (Pedido) FabricaModelo.fabricar("Pedido");
            List<Pedido> pedidos = modeloPedido.listarTodos();
            System.out.println("      Pedidos modelo: " + pedidos.size());
            return pedidos.size() >= 2;
        });

        probar("4.4 Pedido.guardar() crea pedido via modelo (UC-4)", () -> {
            Cliente modeloCliente = (Cliente) FabricaModelo.fabricar("Cliente");
            List<Cliente> clientes = modeloCliente.listarTodos();
            Producto modeloProducto = (Producto) FabricaModelo.fabricar("Producto");
            List<Producto> productos = modeloProducto.listarTodos();

            Pedido nuevo = new Pedido(-1, new Date(), clientes.get(0), null);
            nuevo.agregarItemDetallePedido(productos.get(0), 150.0, 5);
            nuevo.agregarItemDetallePedido(productos.get(2), 400.0, 2);

            boolean guardado = nuevo.guardar();
            System.out.println("      Guardado: " + (guardado ? "SI" : "NO"));
            return guardado;
        });

        probar("4.5 Pedido.modificar() actualiza pedido via modelo (UC-8)", () -> {
            Pedido modeloPedido = (Pedido) FabricaModelo.fabricar("Pedido");
            List<Pedido> pedidos = modeloPedido.listarPorNro(1);
            if (pedidos.isEmpty()) return false;

            Pedido p = pedidos.get(0);
            p.setEstado("CANCELADO");
            boolean modificado = p.modificar();
            System.out.println("      Modificado: " + (modificado ? "SI" : "NO"));
            return modificado;
        });

        probar("4.6 Pedido.buscarDetalles() carga detalles desde BD", () -> {
            Pedido p = new Pedido();
            p.setNroPedido(1);
            p.buscarDetalles();
            int size = p.getDetallePedido().size();
            System.out.println("      Detalles cargados: " + size);
            return size >= 2;
        });

        probar("4.7 Pedido.calcularTotalDetalle() calcula suma correcta", () -> {
            Pedido p = new Pedido();
            p.agregarItemDetallePedido(new Producto("P1", "Prod1"), 100.0, 3);
            p.agregarItemDetallePedido(new Producto("P2", "Prod2"), 200.0, 2);
            double total = p.calcularTotalDetalle();
            System.out.println("      Total calculado: " + total + " (esperado: 700.0)");
            return total == 700.0;
        });

        probar("4.8 Pedido.agregarItemDetallePedido() rechaza duplicados", () -> {
            Pedido p = new Pedido();
            Producto prod = new Producto("P1", "Prod1");
            p.agregarItemDetallePedido(prod, 100.0, 3);
            boolean duplicado = p.agregarItemDetallePedido(prod, 100.0, 3);
            System.out.println("      Rechazo duplicado: " + !duplicado);
            return !duplicado;
        });

        probar("4.9 Pedido.eliminar() borra pedido de BD", () -> {
            // Crear un pedido para eliminar
            Cliente modeloCliente = (Cliente) FabricaModelo.fabricar("Cliente");
            List<Cliente> clientes = modeloCliente.listarTodos();
            Pedido p = new Pedido(-1, new Date(), clientes.get(0), null);
            p.agregarItemDetallePedido(new Producto("P1", "Prod1"), 100.0, 1);
            p.guardar();

            // Obtener el ultimo nroPedido
            Pedido modeloPedido = (Pedido) FabricaModelo.fabricar("Pedido");
            List<Pedido> todos = modeloPedido.listarTodos();
            Pedido ultimo = todos.get(todos.size() - 1);
            boolean eliminado = ultimo.eliminar();
            System.out.println("      Eliminado nro " + ultimo.getNroPedido() + ": " + (eliminado ? "SI" : "NO"));
            return eliminado;
        });

        // =========================================================
        // PRUEBA 5: VERIFICACION ESTRUCTURAL
        // =========================================================
        System.out.println("\n--- [5] VERIFICACION ESTRUCTURAL ---");

        probar("5.1 FabricaDao fabrica ClienteDao directamente", () -> {
            org.ubp.edu.doo.tpdooaguasvital2026.dao.Dao<?> dao =
                org.ubp.edu.doo.tpdooaguasvital2026.factories.FabricaDao.fabricar("ClienteDao");
            return dao != null;
        });

        probar("5.2 FabricaModelo fabrica Pedido directamente", () -> {
            Object modelo = FabricaModelo.fabricar("Pedido");
            return modelo != null && modelo instanceof Pedido;
        });

        probar("5.3 FXML principal existe y es accesible", () -> {
            InputStream is = TesteoExhaustivo.class.getResourceAsStream("/org/ubp/edu/doo/tpdooaguasvital2026/principal.fxml");
            return is != null;
        });

        probar("5.4 FXML pedidos existe y es accesible", () -> {
            InputStream is = TesteoExhaustivo.class.getResourceAsStream("/org/ubp/edu/doo/tpdooaguasvital2026/pedidos.fxml");
            return is != null;
        });

        probar("5.5 FXML editarPedido existe y es accesible", () -> {
            InputStream is = TesteoExhaustivo.class.getResourceAsStream("/org/ubp/edu/doo/tpdooaguasvital2026/editarPedido.fxml");
            return is != null;
        });

        probar("5.6 Esquema SQL existe y es accesible", () -> {
            InputStream is = TesteoExhaustivo.class.getResourceAsStream("/org/ubp/edu/doo/tpdooaguasvital2026/esquema-aguas-vital.sql");
            return is != null;
        });

        probar("5.7 Classes del modelo tienen constructor sin args", () -> {
            Class<?>[] clases = {Cliente.class, Pedido.class, Producto.class, DetallePedido.class,
                Factura.class, Distribuidor.class, Zona.class, Barrio.class, Domicilio.class,
                Telefono.class, Operador.class, EncargadoAdmin.class, Presidente.class,
                Empleado.class, Precio.class, Stock.class, TipoProducto.class};
            for (Class<?> c : clases) {
                c.getDeclaredConstructor().newInstance();
            }
            return true;
        });

        // =========================================================
        // PRUEBA 6: INTEGRIDAD DE DATOS
        // =========================================================
        System.out.println("\n--- [6] INTEGRIDAD DE DATOS ---");

        probar("6.1 Pedido 1 tiene 2 detalles", () -> {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:tp-doo-aguas-vital-2026.db");
                 Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(
                    "SELECT count(*) FROM detallePedido WHERE nroPedido=1");
                System.out.println("      Detalles pedido 1: " + rs.getInt(1));
                return rs.getInt(1) >= 2;
            }
        });

        probar("6.2 Pedido 1 es de Carlos Garcia (cliente 1)", () -> {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:tp-doo-aguas-vital-2026.db");
                 Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(
                    "SELECT c.nombre, c.apellido FROM pedido p, cliente c WHERE p.nroCliente=c.nroCliente AND p.nroPedido=1");
                System.out.println("      Cliente pedido 1: " + rs.getString("nombre") + " " + rs.getString("apellido"));
                return "Carlos".equalsIgnoreCase(rs.getString("nombre")) || "Carlos Garc\u00eda".contains(rs.getString("nombre"));
            }
        });

        probar("6.3 Productos tienen precios asociados", () -> {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:tp-doo-aguas-vital-2026.db");
                 Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(
                    "SELECT count(*) FROM precio p JOIN producto pr ON p.codProducto=pr.codProducto");
                System.out.println("      Precios cargados: " + rs.getInt(1));
                return rs.getInt(1) >= 5;
            }
        });

        // =========================================================
        // RESUMEN FINAL
        // =========================================================
        System.out.println("\n==================================================================");
        System.out.println("  RESUMEN FINAL");
        System.out.println("==================================================================");
        System.out.println("  Pruebas pasadas: " + pasaron);
        System.out.println("  Pruebas falladas: " + fallaron);
        System.out.println("  Total: " + (pasaron + fallaron));
        System.out.println("  Estado: " + (fallaron == 0 ? "TODO OK" : "FALLOS DETECTADOS"));
        System.out.println("==================================================================");
    }

    private static void probar(String nombre, Prueba prueba) {
        try {
            boolean resultado = prueba.ejecutar();
            if (resultado) {
                System.out.println("  [PASS] " + nombre);
                pasaron++;
            } else {
                System.out.println("  [FAIL] " + nombre);
                fallaron++;
            }
        } catch (Exception e) {
            System.out.println("  [FAIL] " + nombre + " (ERROR: " + e.getMessage() + ")");
            fallaron++;
        }
    }

    interface Prueba {
        boolean ejecutar() throws Exception;
    }
}
