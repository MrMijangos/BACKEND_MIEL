package org.API_Miel.MetodoPago.routes;

import org.API_Miel.MetodoPago.controllers.MetodoPagoController;
import io.javalin.Javalin;

public class MetodoPagoRoutes {
    private final MetodoPagoController metodoPagoController;
    
    public MetodoPagoRoutes() {
        this.metodoPagoController = new MetodoPagoController();
    }
    
    public void register(Javalin app) {
        app.post("/api/usuarios/{usuarioId}/metodos-pago", metodoPagoController::create);
        app.get("/api/usuarios/{usuarioId}/metodos-pago", metodoPagoController::getByUsuario);
        app.delete("/api/metodos-pago/{id}", metodoPagoController::delete);
    }
}