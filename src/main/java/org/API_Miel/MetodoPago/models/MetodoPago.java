package org.API_Miel.MetodoPago.models;

import java.sql.Timestamp;

public class MetodoPago {
    private Long id;
    private Long usuarioId;
    private String tipoTarjeta; // 'debito' o 'credito'
    private String nombreTitular;
    private String ultimosDigitos;
    private Integer mesExpiracion;
    private Integer anioExpiracion;
    private Boolean esPredeterminado;
    private Boolean activo;
    private Timestamp fechaCreacion;
    private Timestamp fechaActualizacion;

    // Constructor vacío
    public MetodoPago() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getTipoTarjeta() { return tipoTarjeta; }
    public void setTipoTarjeta(String tipoTarjeta) { this.tipoTarjeta = tipoTarjeta; }

    public String getNombreTitular() { return nombreTitular; }
    public void setNombreTitular(String nombreTitular) { this.nombreTitular = nombreTitular; }

    public String getUltimosDigitos() { return ultimosDigitos; }
    public void setUltimosDigitos(String ultimosDigitos) { this.ultimosDigitos = ultimosDigitos; }

    public Integer getMesExpiracion() { return mesExpiracion; }
    public void setMesExpiracion(Integer mesExpiracion) { this.mesExpiracion = mesExpiracion; }

    public Integer getAnioExpiracion() { return anioExpiracion; }
    public void setAnioExpiracion(Integer anioExpiracion) { this.anioExpiracion = anioExpiracion; }

    public Boolean getEsPredeterminado() { return esPredeterminado; }
    public void setEsPredeterminado(Boolean esPredeterminado) { this.esPredeterminado = esPredeterminado; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Timestamp getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(Timestamp fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}