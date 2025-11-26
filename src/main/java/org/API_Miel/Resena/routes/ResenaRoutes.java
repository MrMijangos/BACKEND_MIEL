package org.API_Miel.Resena.routes;

import io.javalin.Javalin;
import org.API_Miel.Resena.controllers.ResenaController;

public class ResenaRoutes {
    private final ResenaController controller;

    public ResenaRoutes() {
        this.controller = new ResenaController();
    }

    public void registerRoutes(Javalin app) {
        app.post("/api/resenas", controller::crear);
        app.get("/api/productos/{productoId}/resenas", controller::listarPorProducto);
    }
}