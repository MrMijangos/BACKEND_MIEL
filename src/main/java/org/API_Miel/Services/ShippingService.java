package org.API_Miel.Services;

import java.util.List;

import org.API_Miel.Models.Direccion;
import org.API_Miel.Repositories.DireccionRepository;

public class ShippingService {
    private final DireccionRepository repo = new DireccionRepository();

    public List<Direccion> listar(long idUsuario) { return repo.listByUsuario(idUsuario); }
    public long crear(Direccion d) { return repo.create(d); }
    public boolean eliminar(long idDireccion) { return repo.delete(idDireccion); }
}
