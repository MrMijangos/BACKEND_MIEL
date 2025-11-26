package org.API_Miel.Carrito.models;


public class UpdateItemRequest {
    private Integer cantidad;
    
    // Constructor vacío
    public UpdateItemRequest() {}
    
    // Constructor
    public UpdateItemRequest(Integer cantidad) {
        this.cantidad = cantidad;
    }
    
    // Getters y Setters
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}