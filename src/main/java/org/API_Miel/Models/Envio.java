package org.API_Miel.Models;

import java.time.Instant;

public class Envio {
    private long idEnvio;
    private long idPedido;
    private String numeroGuia;
    private String estado;
    private Instant fechaEnvio;

    public long getIdEnvio() { return idEnvio; }
    public void setIdEnvio(long idEnvio) { this.idEnvio = idEnvio; }
    public long getIdPedido() { return idPedido; }
    public void setIdPedido(long idPedido) { this.idPedido = idPedido; }
    public String getNumeroGuia() { return numeroGuia; }
    public void setNumeroGuia(String numeroGuia) { this.numeroGuia = numeroGuia; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Instant getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(Instant fechaEnvio) { this.fechaEnvio = fechaEnvio; }
}