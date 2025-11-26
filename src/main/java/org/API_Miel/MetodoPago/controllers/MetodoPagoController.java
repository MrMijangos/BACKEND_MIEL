package org.API_Miel.MetodoPago.controllers;

import org.API_Miel.MetodoPago.models.CreateMetodoPagoRequest;
import org.API_Miel.MetodoPago.services.MetodoPagoService;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class MetodoPagoController {
    private final MetodoPagoService metodoPagoService;
    
    public MetodoPagoController() {
        this.metodoPagoService = new MetodoPagoService();
    }
    
    public void create(Context ctx) {
        try {
            Long usuarioId = Long.parseLong(ctx.pathParam("usuarioId"));
            CreateMetodoPagoRequest request = ctx.bodyAsClass(CreateMetodoPagoRequest.class);
            
            var metodoPago = metodoPagoService.create(usuarioId, request);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", metodoPago);
            result.put("message", "Método de pago agregado exitosamente");
            
            ctx.status(201).json(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            ctx.status(400).json(error);
        }
    }
    
    public void getByUsuario(Context ctx) {
        try {
            Long usuarioId = Long.parseLong(ctx.pathParam("usuarioId"));
            var metodosPago = metodoPagoService.getByUsuarioId(usuarioId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", metodosPago);
            
            ctx.status(200).json(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            ctx.status(500).json(error);
        }
    }
    
    public void delete(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            metodoPagoService.delete(id);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Método de pago eliminado exitosamente");
            
            ctx.status(200).json(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            ctx.status(404).json(error);
        }
    }
}
