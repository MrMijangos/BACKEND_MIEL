package org.API_Miel.Resena.models;

public class CreateResenaRequest {
    private Long productoId;
    private Long usuarioId; // Generalmente esto se saca del token/sesión, pero lo incluyo por si acaso
    private int calificacion;
    private String comentario;

    // Getters y Setters
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public int getCalificacion() { return calificacion; }
    public void setCalificacion(int calificacion) { this.calificacion = calificacion; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}