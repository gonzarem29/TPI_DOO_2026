package org.ubp.edu.doo.tpdooaguasvital2026.modelo;

public class DetallePedido {
    private Producto producto;
    private double precio;
    private int cantidad;

    public DetallePedido() {}

    public DetallePedido(Producto producto, double precio, int cantidad) {
        this.producto = producto;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double calcularSubtotal() {
        return precio * cantidad;
    }

    public double getSubtotal() {
        return calcularSubtotal();
    }
}
