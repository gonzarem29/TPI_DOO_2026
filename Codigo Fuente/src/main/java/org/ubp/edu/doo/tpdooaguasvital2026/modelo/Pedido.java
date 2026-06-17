package org.ubp.edu.doo.tpdooaguasvital2026.modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.DetallePedidoDto;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.PedidoDto;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.ProductoDto;
import org.ubp.edu.doo.tpdooaguasvital2026.factories.FabricaDao;

public class Pedido extends Modelo {
    private int nroPedido;
    private Date fecha;
    private Date fechaEntrega;
    private Factura factura;
    private String estado;
    private Cliente cliente;
    private Operador operador;
    private Operador opCancela;
    private Distribuidor distribuidor;
    private Zona zona;
    private List<DetallePedido> detallePedido = new ArrayList<>();

    public Pedido() {
        this.dao = FabricaDao.fabricar("PedidoDao");
    }

    public Pedido(int nroPedido, Date fecha, Cliente cliente, Operador operador) {
        this.dao = FabricaDao.fabricar("PedidoDao");
        this.nroPedido = nroPedido;
        this.fecha = fecha;
        this.cliente = cliente;
        this.operador = operador;
        this.estado = Estado.PENDIENTE.name();
    }

    public List<Pedido> listarTodos() {
        List<PedidoDto> dtos = this.dao.listarTodos();
        return Arrays.asList(this.mapper.map(dtos, Pedido[].class));
    }

    public List<Pedido> listarPorNro(int nro) {
        PedidoDto criterio = new PedidoDto();
        criterio.setNroPedido(nro);
        List<PedidoDto> dtos = this.dao.listarPorCriterio(criterio);
        return Arrays.asList(this.mapper.map(dtos, Pedido[].class));
    }

    public boolean guardar() {
        return this.dao.insertar(toPedidoDto());
    }

    public boolean modificar() {
        return this.dao.modificar(toPedidoDto());
    }

    private PedidoDto toPedidoDto() {
        PedidoDto dto = this.mapper.map(this, PedidoDto.class);
        if (this.detallePedido == null || this.detallePedido.isEmpty()) {
            this.buscarDetalles();
        }
        if (this.detallePedido != null && !this.detallePedido.isEmpty()) {
            dto.setDetalles(new ArrayList<>());
            for (DetallePedido det : this.detallePedido) {
                DetallePedidoDto detDto = this.mapper.map(det, DetallePedidoDto.class);
                dto.getDetalles().add(detDto);
            }
        }
        return dto;
    }

    public boolean eliminar() {
        PedidoDto dto = new PedidoDto();
        dto.setNroPedido(this.nroPedido);
        return this.dao.borrar(dto);
    }

    public void buscarDetalles() {
        PedidoDto dto = new PedidoDto();
        dto.setNroPedido(this.nroPedido);
        PedidoDto resultado = (PedidoDto) this.dao.buscar(dto);
        if (resultado != null && resultado.getDetalles() != null) {
            this.detallePedido = new ArrayList<>();
            for (DetallePedidoDto detDto : resultado.getDetalles()) {
                DetallePedido det = this.mapper.map(detDto, DetallePedido.class);
                this.detallePedido.add(det);
            }
        }
    }

    public double calcularTotalDetalle() {
        return detallePedido.stream()
                .mapToDouble(DetallePedido::calcularSubtotal)
                .sum();
    }

    public boolean agregarItemDetallePedido(Producto prod, double precio, int cantidad) {
        for (DetallePedido det : this.detallePedido) {
            if (det.getProducto().getCodProducto().equals(prod.getCodProducto())) {
                return false;
            }
        }
        this.detallePedido.add(new DetallePedido(prod, precio, cantidad));
        return true;
    }

    public int getNroPedido() { return nroPedido; }
    public void setNroPedido(int nroPedido) { this.nroPedido = nroPedido; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public Date getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(Date fechaEntrega) { this.fechaEntrega = fechaEntrega; }
    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Operador getOperador() { return operador; }
    public void setOperador(Operador operador) { this.operador = operador; }
    public Operador getOpCancela() { return opCancela; }
    public void setOpCancela(Operador opCancela) { this.opCancela = opCancela; }
    public Distribuidor getDistribuidor() { return distribuidor; }
    public void setDistribuidor(Distribuidor distribuidor) { this.distribuidor = distribuidor; }
    public Zona getZona() { return zona; }
    public void setZona(Zona zona) { this.zona = zona; }
    public List<DetallePedido> getDetallePedido() { return detallePedido; }
    public void setDetallePedido(List<DetallePedido> detallePedido) { this.detallePedido = detallePedido; }
    public List<DetallePedido> getDetalles() { return detallePedido; }
    public void setDetalles(List<DetallePedido> detalles) { this.detallePedido = detalles; }

    public double calcularTotal() {
        return detallePedido.stream()
                .mapToDouble(DetallePedido::calcularSubtotal)
                .sum();
    }

    public void asignarDistribuidor(Distribuidor distribuidor) {
        this.distribuidor = distribuidor;
    }
}
