package org.API_Miel.Carrito.controllers;

import org.API_Miel.Carrito.models.AddItemRequest;
import org.API_Miel.Carrito.models.UpdateItemRequest;
import org.API_Miel.Carrito.models.CarritoResponse;
import org.API_Miel.Carrito.services.CarritoService;
import io.javalin.http.Context;

public class CarritoController {
    private final CarritoService service;
    
    public CarritoController() {
        this.service = new CarritoService();
    }
    
    public void getCarrito(Context ctx) {
        try {
            Long usuarioId = Long.parseLong(ctx.pathParam("usuarioId"));
            CarritoResponse carrito = service.getCarritoByUsuario(usuarioId);
            ctx.json(carrito);
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ErrorResponse("ID de usuario inválido"));
        } catch (Exception e) {
            ctx.status(500).json(new ErrorResponse("Error al obtener carrito: " + e.getMessage()));
        }
    }
    
    public void addItem(Context ctx) {
        try {
            Long usuarioId = Long.parseLong(ctx.pathParam("usuarioId"));
            AddItemRequest request = ctx.bodyAsClass(AddItemRequest.class);
            CarritoResponse carrito = service.addItem(usuarioId, request);
            ctx.status(201).json(carrito);
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            ctx.status(500).json(new ErrorResponse("Error al agregar item: " + e.getMessage()));
        }
    }
    
    public void updateItem(Context ctx) {
        try {
            Long usuarioId = Long.parseLong(ctx.pathParam("usuarioId"));
            Long productoId = Long.parseLong(ctx.pathParam("productoId"));
            UpdateItemRequest request = ctx.bodyAsClass(UpdateItemRequest.class);
            CarritoResponse carrito = service.updateItem(usuarioId, productoId, request);
            ctx.json(carrito);
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            ctx.status(500).json(new ErrorResponse("Error al actualizar item: " + e.getMessage()));
        }
    }
    
    public void removeItem(Context ctx) {
        try {
            Long usuarioId = Long.parseLong(ctx.pathParam("usuarioId"));
            Long productoId = Long.parseLong(ctx.pathParam("productoId"));
            CarritoResponse carrito = service.removeItem(usuarioId, productoId);
            ctx.json(carrito);
        } catch (IllegalArgumentException e) {
            ctx.status(404).json(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            ctx.status(500).json(new ErrorResponse("Error al eliminar item: " + e.getMessage()));
        }
    }
    
    public void clearCarrito(Context ctx) {
        try {
            Long usuarioId = Long.parseLong(ctx.pathParam("usuarioId"));
            service.clearCarrito(usuarioId);
            ctx.status(204);
        } catch (Exception e) {
            ctx.status(500).json(new ErrorResponse("Error al limpiar carrito: " + e.getMessage()));
        }
    }
    
    record ErrorResponse(String error) {}
}