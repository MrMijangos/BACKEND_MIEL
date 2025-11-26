package org.API_Miel.MetodoPago.models;

public class CreateMetodoPagoRequest {
    private String tipoTarjeta;
    private String nombreTitular;
    private String numeroTarjeta; // Completo para procesamiento, solo guardaremos últimos 4 dígitos
    private Integer mesExpiracion;
    private Integer anioExpiracion;
    private Boolean esPredeterminado;

    // Getters y Setters
    public String getTipoTarjeta() { return tipoTarjeta; }
    public void setTipoTarjeta(String tipoTarjeta) { this.tipoTarjeta = tipoTarjeta; }

    public String getNombreTitular() { return nombreTitular; }
    public void setNombreTitular(String nombreTitular) { this.nombreTitular = nombreTitular; }

    public String getNumeroTarjeta() { return numeroTarjeta; }
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }

    public Integer getMesExpiracion() { return mesExpiracion; }
    public void setMesExpiracion(Integer mesExpiracion) { this.mesExpiracion = mesExpiracion; }

    public Integer getAnioExpiracion() { return anioExpiracion; }
    public void setAnioExpiracion(Integer anioExpiracion) { this.anioExpiracion = anioExpiracion; }

    public Boolean getEsPredeterminado() { return esPredeterminado; }
    public void setEsPredeterminado(Boolean esPredeterminado) { this.esPredeterminado = esPredeterminado; }
}