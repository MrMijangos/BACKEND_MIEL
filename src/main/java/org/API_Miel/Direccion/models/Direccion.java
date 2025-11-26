package org.API_Miel.Direccion.models;

import java.sql.Timestamp;

public class Direccion {
    private Long id;
    private Long usuarioId;
    private String nombreDestinatario;
    private String telefonoContacto;
    private String calle;
    private String numeroExterior;
    private String numeroInterior;
    private String colonia;
    private String codigoPostal;
    private String ciudad;
    private String estado;
    private String referencias;
    private Boolean esPredeterminada;
    private Boolean activa;
    private Timestamp fechaCreacion;
    private Timestamp fechaActualizacion;

    // Constructor vacío
    public Direccion() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getNombreDestinatario() { return nombreDestinatario; }
    public void setNombreDestinatario(String nombreDestinatario) { this.nombreDestinatario = nombreDestinatario; }

    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String telefonoContacto) { this.telefonoContacto = telefonoContacto; }

    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }

    public String getNumeroExterior() { return numeroExterior; }
    public void setNumeroExterior(String numeroExterior) { this.numeroExterior = numeroExterior; }

    public String getNumeroInterior() { return numeroInterior; }
    public void setNumeroInterior(String numeroInterior) { this.numeroInterior = numeroInterior; }

    public String getColonia() { return colonia; }
    public void setColonia(String colonia) { this.colonia = colonia; }

    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getReferencias() { return referencias; }
    public void setReferencias(String referencias) { this.referencias = referencias; }

    public Boolean getEsPredeterminada() { return esPredeterminada; }
    public void setEsPredeterminada(Boolean esPredeterminada) { this.esPredeterminada = esPredeterminada; }

    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }

    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Timestamp getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(Timestamp fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}