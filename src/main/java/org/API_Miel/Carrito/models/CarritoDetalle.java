package org.API_Miel.Carrito.models;

import java.time.LocalDateTime;

public class CarritoDetalle {
    private Long id;
    private Long carritoId;
    private Long productoId;
    private Integer cantidad;
    private LocalDateTime fechaAgregado;
    
    // Constructor vacío
    public CarritoDetalle() {}
    
    // Constructor completo
    public CarritoDetalle(Long id, Long carritoId, Long productoId, Integer cantidad, 
                         LocalDateTime fechaAgregado) {
        this.id = id;
        this.carritoId = carritoId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.fechaAgregado = fechaAgregado;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getCarritoId() { return carritoId; }
    public void setCarritoId(Long carritoId) { this.carritoId = carritoId; }
    
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    
    public LocalDateTime getFechaAgregado() { return fechaAgregado; }
    public void setFechaAgregado(LocalDateTime fechaAgregado) { this.fechaAgregado = fechaAgregado; }
}