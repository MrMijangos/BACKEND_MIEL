package org.API_Miel.Pedido.models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class PedidoResponse {
    private Long id;
    private String numeroPedido;
    private BigDecimal subtotal;
    private BigDecimal costoEnvio;
    private BigDecimal total;
    private String estado;
    private String notasCliente;
    private Timestamp fechaPedido;
    private Timestamp fechaEntrega;
    private List<PedidoDetalle> detalles;

    // Constructor
    public PedidoResponse() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroPedido() { return numeroPedido; }
    public void setNumeroPedido(String numeroPedido) { this.numeroPedido = numeroPedido; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getCostoEnvio() { return costoEnvio; }
    public void setCostoEnvio(BigDecimal costoEnvio) { this.costoEnvio = costoEnvio; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getNotasCliente() { return notasCliente; }
    public void setNotasCliente(String notasCliente) { this.notasCliente = notasCliente; }

    public Timestamp getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(Timestamp fechaPedido) { this.fechaPedido = fechaPedido; }

    public Timestamp getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(Timestamp fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public List<PedidoDetalle> getDetalles() { return detalles; }
    public void setDetalles(List<PedidoDetalle> detalles) { this.detalles = detalles; }
}