package org.API_Miel.Usuario.models;

public class UpdateUsuarioRequest {
    private String nombreCompleto;
    private String numCelular;

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getNumCelular() { return numCelular; }
    public void setNumCelular(String numCelular) { this.numCelular = numCelular; }
}   