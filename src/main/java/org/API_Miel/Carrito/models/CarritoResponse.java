package org.API_Miel.Carrito.models;

import java.math.BigDecimal;
import java.util.List;

public class CarritoResponse {
    private Long carritoId;
    private Long usuarioId;
    private List<CarritoDetalleDTO> items;
    private Integer totalItems;
    private BigDecimal totalGeneral;
    
    // Constructor vacío
    public CarritoResponse() {}
    
    // Constructor completo
    public CarritoResponse(Long carritoId, Long usuarioId, List<CarritoDetalleDTO> items, 
                          Integer totalItems, BigDecimal totalGeneral) {
        this.carritoId = carritoId;
        this.usuarioId = usuarioId;
        this.items = items;
        this.totalItems = totalItems;
        this.totalGeneral = totalGeneral;
    }
    
    // Getters y Setters
    public Long getCarritoId() { return carritoId; }
    public void setCarritoId(Long carritoId) { this.carritoId = carritoId; }
    
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    
    public List<CarritoDetalleDTO> getItems() { return items; }
    public void setItems(List<CarritoDetalleDTO> items) { this.items = items; }
    
    public Integer getTotalItems() { return totalItems; }
    public void setTotalItems(Integer totalItems) { this.totalItems = totalItems; }
    
    public BigDecimal getTotalGeneral() { return totalGeneral; }
    public void setTotalGeneral(BigDecimal totalGeneral) { this.totalGeneral = totalGeneral; }
}