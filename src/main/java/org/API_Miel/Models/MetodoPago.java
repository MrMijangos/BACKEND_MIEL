package org.API_Miel.Models;

public class MetodoPago {
    private long idMetodoPago;
    private long idUsuario;
    private String tipo;
    private String detalles;

    public long getIdMetodoPago() { return idMetodoPago; }
    public void setIdMetodoPago(long idMetodoPago) { this.idMetodoPago = idMetodoPago; }
    public long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(long idUsuario) { this.idUsuario = idUsuario; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDetalles() { return detalles; }
    public void setDetalles(String detalles) { this.detalles = detalles; }
}