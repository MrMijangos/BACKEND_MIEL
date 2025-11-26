package org.API_Miel.Carrito.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CarritoDetalleDTO {
    private Long id;
    private Long carritoId;
    private Long productoId;
    private String nombreProducto;
    private String descripcionProducto;
    private BigDecimal precioUnitario;
    private String imagenUrl;
    private Integer cantidad;
    private BigDecimal subtotal;
    private LocalDateTime fechaAgregado;
    
    // Constructor vacío
    public CarritoDetalleDTO() {}
    
    // Constructor completo
    public CarritoDetalleDTO(Long id, Long carritoId, Long productoId, String nombreProducto,
                            String descripcionProducto, BigDecimal precioUnitario, String imagenUrl,
                            Integer cantidad, BigDecimal subtotal, LocalDateTime fechaAgregado) {
        this.id = id;
        this.carritoId = carritoId;
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.precioUnitario = precioUnitario;
        this.imagenUrl = imagenUrl;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.fechaAgregado = fechaAgregado;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getCarritoId() { return carritoId; }
    public void setCarritoId(Long carritoId) { this.carritoId = carritoId; }
    
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    
    public String getDescripcionProducto() { return descripcionProducto; }
    public void setDescripcionProducto(String descripcionProducto) { 
        this.descripcionProducto = descripcionProducto; 
    }
    
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    
    public LocalDateTime getFechaAgregado() { return fechaAgregado; }
    public void setFechaAgregado(LocalDateTime fechaAgregado) { this.fechaAgregado = fechaAgregado; }
}