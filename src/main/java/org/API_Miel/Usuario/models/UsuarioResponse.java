package org.API_Miel.Usuario.models;

import java.sql.Timestamp;

public class UsuarioResponse {
    private Long id;
    private String nombreCompleto;
    private String correo;
    private String numCelular;
    private Integer rolId;
    private Boolean activo;
    private Timestamp fechaRegistro;

    public UsuarioResponse(Usuario usuario) {
        this.id = usuario.getId();
        this.nombreCompleto = usuario.getNombreCompleto();
        this.correo = usuario.getCorreo();
        this.numCelular = usuario.getNumCelular();
        this.rolId = usuario.getRolId();
        this.activo = usuario.getActivo();
        this.fechaRegistro = usuario.getFechaRegistro();
    }

    // Getters
    public Long getId() { return id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getCorreo() { return correo; }
    public String getNumCelular() { return numCelular; }
    public Integer getRolId() { return rolId; }
    public Boolean getActivo() { return activo; }
    public Timestamp getFechaRegistro() { return fechaRegistro; }
}