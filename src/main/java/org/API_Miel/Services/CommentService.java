package org.API_Miel.Services;

import org.API_Miel.Models.Resena;
import org.API_Miel.Repositories.ResenaRepository;

import java.util.List;

public class CommentService {
    private final ResenaRepository repo = new ResenaRepository();

    public List<Resena> listarPorProducto(long idProducto) {
        return repo.listByProducto(idProducto);
    }

    public long crear(Resena r) {
        return repo.create(r);
    }

    public void eliminar(long idResena) {
        repo.delete(idResena);
    }
}
