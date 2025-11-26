package org.API_Miel.Direccion.routes;

import org.API_Miel.Direccion.controllers.DireccionController;
import io.javalin.Javalin;

public class DireccionRoutes {
    private final DireccionController direccionController;
    
    public DireccionRoutes() {
        this.direccionController = new DireccionController();
    }
    
    public void register(Javalin app) {
        app.post("/api/usuarios/{usuarioId}/direcciones", direccionController::create);
        app.get("/api/usuarios/{usuarioId}/direcciones", direccionController::getByUsuario);
        app.get("/api/direcciones/{id}", direccionController::getById);
        app.put("/api/direcciones/{id}", direccionController::update);
        app.delete("/api/direcciones/{id}", direccionController::delete);
    }
}