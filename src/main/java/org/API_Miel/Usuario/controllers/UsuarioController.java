package org.API_Miel.Usuario.controllers;

import org.API_Miel.Usuario.models.LoginRequest;
import org.API_Miel.Usuario.models.RegisterRequest;
import org.API_Miel.Usuario.models.UpdateUsuarioRequest;
import org.API_Miel.Usuario.models.UsuarioResponse;
import org.API_Miel.Usuario.services.UsuarioService;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class UsuarioController {
    private final UsuarioService usuarioService;
    
    public UsuarioController() {
        this.usuarioService = new UsuarioService();
    }
    
    public void register(Context ctx) {
        try {
            RegisterRequest request = ctx.bodyAsClass(RegisterRequest.class);
            UsuarioResponse response = usuarioService.register(request);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", response);
            result.put("message", "Usuario registrado exitosamente");
            
            ctx.status(201).json(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            ctx.status(400).json(error);
        }
    }
    
    public void login(Context ctx) {
        try {
            LoginRequest request = ctx.bodyAsClass(LoginRequest.class);
            UsuarioResponse response = usuarioService.login(request);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", response);
            result.put("message", "Login exitoso");
            
            ctx.status(200).json(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            ctx.status(401).json(error);
        }
    }
    
    public void getById(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            UsuarioResponse response = usuarioService.getById(id);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", response);
            
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
            UpdateUsuarioRequest request = ctx.bodyAsClass(UpdateUsuarioRequest.class);
            UsuarioResponse response = usuarioService.update(id, request);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", response);
            result.put("message", "Usuario actualizado exitosamente");
            
            ctx.status(200).json(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            ctx.status(400).json(error);
        }
    }
    
    public void getAll(Context ctx) {
        try {
            var usuarios = usuarioService.getAll();
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", usuarios);
            
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
            usuarioService.delete(id);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Usuario desactivado exitosamente");
            
            ctx.status(200).json(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            ctx.status(404).json(error);
        }
    }
}