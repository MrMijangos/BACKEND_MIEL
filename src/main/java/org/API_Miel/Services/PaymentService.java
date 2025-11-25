package org.API_Miel.Services;

import java.util.List;

import org.API_Miel.Models.MetodoPago;
import org.API_Miel.Repositories.MetodoPagoRepository;

public class PaymentService {
    private final MetodoPagoRepository repo = new MetodoPagoRepository();

    public List<MetodoPago> listar(long idUsuario) { return repo.listByUsuario(idUsuario); }
    public long crear(MetodoPago m) { return repo.create(m); }
    public boolean eliminar(long idMetodoPago) { return repo.delete(idMetodoPago); }
}
