package org.API_Miel.MetodoPago.services;

import org.API_Miel.MetodoPago.models.CreateMetodoPagoRequest;
import org.API_Miel.MetodoPago.models.MetodoPago;
import org.API_Miel.MetodoPago.repositories.MetodoPagoRepository;
import java.util.List;

public class MetodoPagoService {
    private final MetodoPagoRepository metodoPagoRepository;
    
    public MetodoPagoService() {
        this.metodoPagoRepository = new MetodoPagoRepository();
    }
    
    public MetodoPago create(Long usuarioId, CreateMetodoPagoRequest request) {
        // Validar número de tarjeta (básico)
        if (request.getNumeroTarjeta() == null || request.getNumeroTarjeta().length() < 13) {
            throw new RuntimeException("Número de tarjeta inválido");
        }
        
        // Extraer últimos 4 dígitos
        String ultimosDigitos = request.getNumeroTarjeta().substring(
            request.getNumeroTarjeta().length() - 4
        );
        
        MetodoPago metodoPago = new MetodoPago();
        metodoPago.setUsuarioId(usuarioId);
        metodoPago.setTipoTarjeta(request.getTipoTarjeta());
        metodoPago.setNombreTitular(request.getNombreTitular());
        metodoPago.setUltimosDigitos(ultimosDigitos);
        metodoPago.setMesExpiracion(request.getMesExpiracion());
        metodoPago.setAnioExpiracion(request.getAnioExpiracion());
        metodoPago.setEsPredeterminado(request.getEsPredeterminado() != null ? request.getEsPredeterminado() : false);
        metodoPago.setActivo(true);
        
        // Si es predeterminado, desmarcar los demás
        if (metodoPago.getEsPredeterminado()) {
            metodoPagoRepository.unsetPredeterminado(usuarioId);
        }
        
        return metodoPagoRepository.save(metodoPago);
    }
    
    public List<MetodoPago> getByUsuarioId(Long usuarioId) {
        return metodoPagoRepository.findByUsuarioId(usuarioId);
    }
    
    public MetodoPago getById(Long id) {
        return metodoPagoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));
    }
    
    public void delete(Long id) {
        metodoPagoRepository.delete(id);
    }
}