package org.API_Miel.Resena.controllers;

import io.javalin.http.Context;
import org.API_Miel.Resena.models.CreateResenaRequest;
import org.API_Miel.Resena.services.ResenaService;

public class ResenaController {
    private final ResenaService service;

    public ResenaController() {
        this.service = new ResenaService();
    }

    public void crear(Context ctx) {
        try {
            CreateResenaRequest request = ctx.bodyAsClass(CreateResenaRequest.class);
            request.setUsuarioId(ctx.attribute("usuario_id")); 

            service.crearResena(request);
            ctx.status(201).json("Reseña creada exitosamente");
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).json("Error al crear la reseña: " + e.getMessage());
        }
    }

    public void listarPorProducto(Context ctx) {
        try {
            Long productoId = Long.parseLong(ctx.pathParam("productoId"));
            ctx.json(service.obtenerResenasPorProducto(productoId));
        } catch (Exception e) {
            ctx.status(500).json("Error al obtener reseñas");
        }
    }
}