package org.API_Miel.Direccion.controllers;

import io.javalin.http.Context;
import org.API_Miel.Direccion.models.CreateDireccionRequest;
import org.API_Miel.Direccion.services.DireccionService;

import java.util.HashMap;
import java.util.Map;

public class DireccionController {
    private final DireccionService direccionService;
    
    public DireccionController() {
        this.direccionService = new DireccionService();
    }
    
    public void create(Context ctx) {
        try {
            Long usuarioId = Long.parseLong(ctx.pathParam("usuarioId"));
            CreateDireccionRequest request = ctx.bodyAsClass(CreateDireccionRequest.class);
            
            var direccion = direccionService.create(usuarioId, request);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", direccion);
            result.put("message", "Dirección creada exitosamente");
            
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
            var direcciones = direccionService.getByUsuarioId(usuarioId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", direcciones);
            
            ctx.status(200).json(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            ctx.status(500).json(error);
        }
    }
    
    public void getById(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            var direccion = direccionService.getById(id);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", direccion);
            
            ctx.status(200).json(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            ctx.status(404).json(error);
        }
    }
    
    public void update(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            CreateDireccionRequest request = ctx.bodyAsClass(CreateDireccionRequest.class);
            
            var direccion = direccionService.update(id, request);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", direccion);
            result.put("message", "Dirección actualizada exitosamente");
            
            ctx.status(200).json(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            ctx.status(400).json(error);
        }
    }
    
    public void delete(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            direccionService.delete(id);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Dirección eliminada exitosamente");
            
            ctx.status(200).json(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            ctx.status(404).json(error);
        }
    }
}