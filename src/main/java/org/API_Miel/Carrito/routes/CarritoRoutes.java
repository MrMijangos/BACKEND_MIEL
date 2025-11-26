package org.API_Miel.Carrito.routes;

import org.API_Miel.Carrito.controllers.CarritoController;
import io.javalin.Javalin;

public class CarritoRoutes {
    private static final CarritoController controller = new CarritoController();
    
    public static void register(Javalin app) {
        // Obtener carrito del usuario
        app.get("/api/carritos/usuario/{usuarioId}", controller::getCarrito);
        
        // Agregar item al carrito
        app.post("/api/carritos/usuario/{usuarioId}/items", controller::addItem);
        
        // Actualizar cantidad de un item
        app.put("/api/carritos/usuario/{usuarioId}/items/{productoId}", controller::updateItem);
        
        // Eliminar item del carrito
        app.delete("/api/carritos/usuario/{usuarioId}/items/{productoId}", controller::removeItem);
        
        // Limpiar todo el carrito
        app.delete("/api/carritos/usuario/{usuarioId}", controller::clearCarrito);
    }
}