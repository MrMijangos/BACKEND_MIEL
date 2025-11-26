package org.API_Miel.Producto.routes;

import org.API_Miel.Producto.controllers.ProductoController;
import io.javalin.Javalin;

public class ProductoRoutes {
    private static final ProductoController controller = new ProductoController();
    
    public static void register(Javalin app) {
        // CRUD básico
        app.post("/api/productos", controller::create);
        app.get("/api/productos", controller::getAll);
        app.get("/api/productos/search", controller::search);
        app.get("/api/productos/{id}", controller::getById);
        app.put("/api/productos/{id}", controller::update);
        app.delete("/api/productos/{id}", controller::delete);
        
        // Gestión de stock
        app.patch("/api/productos/{id}/stock", controller::updateStock);
    }
}
