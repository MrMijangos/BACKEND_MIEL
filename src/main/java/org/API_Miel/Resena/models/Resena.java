package org.API_Miel.Resena.models;

import java.sql.Timestamp;

public class Resena {
    private Long id;
    private Long productoId;
    private Long usuarioId;
    private int calificacion; // TINYINT en SQL
    private String comentario;
    private boolean activa;
    private Timestamp fechaCreacion;

    public Resena() {}

    public Resena(Long id, Long productoId, Long usuarioId, int calificacion, String comentario, boolean activa, Timestamp fechaCreacion) {
        this.id = id;
        this.productoId = productoId;
        this.usuarioId = usuarioId;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.activa = activa;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public int getCalificacion() { return calificacion; }
    public void setCalificacion(int calificacion) { this.calificacion = calificacion; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}