package org.API_Miel.Pedido.models;

public class CreatePedidoRequest {
    private Long metodoPagoId;
    private Long direccionId;
    private String notasCliente;

    // Getters y Setters
    public Long getMetodoPagoId() { return metodoPagoId; }
    public void setMetodoPagoId(Long metodoPagoId) { this.metodoPagoId = metodoPagoId; }

    public Long getDireccionId() { return direccionId; }
    public void setDireccionId(Long direccionId) { this.direccionId = direccionId; }

    public String getNotasCliente() { return notasCliente; }
    public void setNotasCliente(String notasCliente) { this.notasCliente = notasCliente; }
}