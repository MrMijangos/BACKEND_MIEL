package org.API_Miel.Pedido.routes;

import org.API_Miel.Pedido.controllers.PedidoController;
import io.javalin.Javalin;

public class PedidoRoutes {
    private final PedidoController pedidoController;
    
    public PedidoRoutes() {
        this.pedidoController = new PedidoController();
    }
    
    public void register(Javalin app) {
        app.post("/api/usuarios/{usuarioId}/pedidos", pedidoController::create);
        app.get("/api/usuarios/{usuarioId}/pedidos", pedidoController::getByUsuario);
        app.get("/api/pedidos", pedidoController::getAll);
        app.get("/api/pedidos/{id}", pedidoController::getById);
        app.patch("/api/pedidos/{id}/estado", pedidoController::updateEstado);
    }
}