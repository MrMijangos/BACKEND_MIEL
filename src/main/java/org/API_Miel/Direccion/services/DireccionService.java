package org.API_Miel.Direccion.services;

import org.API_Miel.Direccion.models.CreateDireccionRequest;
import org.API_Miel.Direccion.models.Direccion;
import org.API_Miel.Direccion.repositories.DireccionRepository;
import java.util.List;

public class DireccionService {
    private final DireccionRepository direccionRepository;
    
    public DireccionService() {
        this.direccionRepository = new DireccionRepository();
    }
    
    public Direccion create(Long usuarioId, CreateDireccionRequest request) {
        Direccion direccion = new Direccion();
        direccion.setUsuarioId(usuarioId);
        direccion.setNombreDestinatario(request.getNombreDestinatario());
        direccion.setTelefonoContacto(request.getTelefonoContacto());
        direccion.setCalle(request.getCalle());
        direccion.setNumeroExterior(request.getNumeroExterior());
        direccion.setNumeroInterior(request.getNumeroInterior());
        direccion.setColonia(request.getColonia());
        direccion.setCodigoPostal(request.getCodigoPostal());
        direccion.setCiudad(request.getCiudad());
        direccion.setEstado(request.getEstado());
        direccion.setReferencias(request.getReferencias());
        direccion.setEsPredeterminada(request.getEsPredeterminada() != null ? request.getEsPredeterminada() : false);
        direccion.setActiva(true);
        
        // Si es predeterminada, desmarcar las demás
        if (direccion.getEsPredeterminada()) {
            direccionRepository.unsetPredeterminada(usuarioId);
        }
        
        return direccionRepository.save(direccion);
    }
    
    public List<Direccion> getByUsuarioId(Long usuarioId) {
        return direccionRepository.findByUsuarioId(usuarioId);
    }
    
    public Direccion getById(Long id) {
        return direccionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));
    }
    
    public Direccion update(Long id, CreateDireccionRequest request) {
        Direccion direccion = direccionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));
        
        direccion.setNombreDestinatario(request.getNombreDestinatario());
        direccion.setTelefonoContacto(request.getTelefonoContacto());
        direccion.setCalle(request.getCalle());
        direccion.setNumeroExterior(request.getNumeroExterior());
        direccion.setNumeroInterior(request.getNumeroInterior());
        direccion.setColonia(request.getColonia());
        direccion.setCodigoPostal(request.getCodigoPostal());
        direccion.setCiudad(request.getCiudad());
        direccion.setEstado(request.getEstado());
        direccion.setReferencias(request.getReferencias());
        direccion.setEsPredeterminada(request.getEsPredeterminada() != null ? request.getEsPredeterminada() : false);
        
        // Si es predeterminada, desmarcar las demás
        if (direccion.getEsPredeterminada()) {
            direccionRepository.unsetPredeterminada(direccion.getUsuarioId());
        }
        
        return direccionRepository.update(direccion);
    }
    
    public void delete(Long id) {
        direccionRepository.delete(id);
    }
}