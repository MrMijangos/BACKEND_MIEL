package org.API_Miel.Models;

import java.time.Instant;

public class Resena {
    private long idResena;
    private long idProducto;
    private long idUsuario;
    private int calificacion;
    private String comentario;
    private Instant fecha;

    public long getIdResena() { return idResena; }
    public void setIdResena(long idResena) { this.idResena = idResena; }
    public long getIdProducto() { return idProducto; }
    public void setIdProducto(long idProducto) { this.idProducto = idProducto; }
    public long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(long idUsuario) { this.idUsuario = idUsuario; }
    public int getCalificacion() { return calificacion; }
    public void setCalificacion(int calificacion) { this.calificacion = calificacion; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public Instant getFecha() { return fecha; }
    public void setFecha(Instant fecha) { this.fecha = fecha; }
}