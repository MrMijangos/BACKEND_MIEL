package org.API_Miel.Models;

public class Direccion {
    private long idDireccion;
    private long idUsuario;
    private String calle;
    private String colonia;
    private String codigoPostal;
    private String ciudad;
    private String estado;

    public long getIdDireccion() { return idDireccion; }
    public void setIdDireccion(long idDireccion) { this.idDireccion = idDireccion; }
    public long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(long idUsuario) { this.idUsuario = idUsuario; }
    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }
    public String getColonia() { return colonia; }
    public void setColonia(String colonia) { this.colonia = colonia; }
    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}