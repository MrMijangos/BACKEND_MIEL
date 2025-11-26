package org.API_Miel.Resena.services;

import org.API_Miel.Resena.models.CreateResenaRequest;
import org.API_Miel.Resena.models.Resena;
import org.API_Miel.Resena.repositories.ResenaRepository;
import java.sql.SQLException;
import java.util.List;

public class ResenaService {
    private final ResenaRepository repository;

    public ResenaService() {
        this.repository = new ResenaRepository();
    }

    public void crearResena(CreateResenaRequest request) throws Exception {
        // Validaciones de negocio
        if (request.getCalificacion() < 1 || request.getCalificacion() > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5");
        }
        if (request.getComentario() == null || request.getComentario().trim().isEmpty()) {
            throw new IllegalArgumentException("El comentario no puede estar vacío");
        }

        // Convertir DTO a Entidad
        Resena resena = new Resena();
        resena.setProductoId(request.getProductoId());
        resena.setUsuarioId(request.getUsuarioId());
        resena.setCalificacion(request.getCalificacion());
        resena.setComentario(request.getComentario());

        try {
            repository.crear(resena);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Código de error MySQL para entrada duplicada
                throw new Exception("Ya has dejado una reseña para este producto.");
            }
            throw e;
        }
    }

    public List<Resena> obtenerResenasPorProducto(Long productoId) throws SQLException {
        return repository.buscarPorProducto(productoId);
    }
}