package org.ubp.edu.doo.tpdooaguasvital2026.dto;

public class DetallePedidoDto {
    private ProductoDto producto;
    private double precio;
    private int cantidad;

    public DetallePedidoDto() {}

    public DetallePedidoDto(ProductoDto producto, double precio, int cantidad) {
        this.producto = producto;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public ProductoDto getProducto() { return producto; }
    public void setProducto(ProductoDto producto) { this.producto = producto; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
