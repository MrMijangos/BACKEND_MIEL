package org.API_Miel.Usuario.models;
import java.sql.Timestamp;

public class Usuario {
    private Long id;
    private String nombreCompleto;
    private String correo;
    private String contrasena;
    private String numCelular;
    private Integer rolId;
    private Boolean activo;
    private Timestamp fechaRegistro;
    private Timestamp ultimaConexion;

    // Constructor vacío
    public Usuario() {}

    // Constructor completo
    public Usuario(Long id, String nombreCompleto, String correo, String contrasena, 
                   String numCelular, Integer rolId, Boolean activo, 
                   Timestamp fechaRegistro, Timestamp ultimaConexion) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.contrasena = contrasena;
        this.numCelular = numCelular;
        this.rolId = rolId;
        this.activo = activo;
        this.fechaRegistro = fechaRegistro;
        this.ultimaConexion = ultimaConexion;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getNumCelular() { return numCelular; }
    public void setNumCelular(String numCelular) { this.numCelular = numCelular; }

    public Integer getRolId() { return rolId; }
    public void setRolId(Integer rolId) { this.rolId = rolId; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public Timestamp getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Timestamp fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public Timestamp getUltimaConexion() { return ultimaConexion; }
    public void setUltimaConexion(Timestamp ultimaConexion) { this.ultimaConexion = ultimaConexion; }
}