package org.API_Miel.Models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class Pedido {
    private long idPedido;
    private String numeroPedido;
    private long idUsuario;
    private long idMetodoPago;
    private long idDireccion;
    private BigDecimal total;
    private String estado;
    private Instant fecha;
    private List<Map<String, Object>> detalles; 
    
    
    public long getIdPedido() { return idPedido; }
    public void setIdPedido(long idPedido) { this.idPedido = idPedido; }
    
    public String getNumeroPedido() { return numeroPedido; }
    public void setNumeroPedido(String numeroPedido) { this.numeroPedido = numeroPedido; }
    
    public long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(long idUsuario) { this.idUsuario = idUsuario; }
    
    public long getIdMetodoPago() { return idMetodoPago; }
    public void setIdMetodoPago(long idMetodoPago) { this.idMetodoPago = idMetodoPago; }
    
    public long getIdDireccion() { return idDireccion; }
    public void setIdDireccion(long idDireccion) { this.idDireccion = idDireccion; }
    
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public Instant getFecha() { return fecha; }
    public void setFecha(Instant fecha) { this.fecha = fecha; }
    
    //  NUEVO: Getter y Setter para detalles
    public List<Map<String, Object>> getDetalles() { return detalles; }
    public void setDetalles(List<Map<String, Object>> detalles) { this.detalles = detalles; }
}