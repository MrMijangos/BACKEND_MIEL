package org.API_Miel.Carrito.models;

public class AddItemRequest {
    private Long productoId;
    private Integer cantidad;
    
    // Constructor vacío
    public AddItemRequest() {}
    
    // Constructor
    public AddItemRequest(Long productoId, Integer cantidad) {
        this.productoId = productoId;
        this.cantidad = cantidad;
    }
    
    // Getters y Setters
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}