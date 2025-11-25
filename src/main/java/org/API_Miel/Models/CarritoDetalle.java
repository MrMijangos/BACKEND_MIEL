package org.API_Miel.Models;

public class CarritoDetalle {
    private long idDetalle;
    private long idCarrito;
    private long idProducto;
    private int cantidad;

    public long getIdDetalle() { return idDetalle; }
    public void setIdDetalle(long idDetalle) { this.idDetalle = idDetalle; }
    public long getIdCarrito() { return idCarrito; }
    public void setIdCarrito(long idCarrito) { this.idCarrito = idCarrito; }
    public long getIdProducto() { return idProducto; }
    public void setIdProducto(long idProducto) { this.idProducto = idProducto; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}