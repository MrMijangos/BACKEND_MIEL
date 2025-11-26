package org.API_Miel;

import io.javalin.Javalin;
import org.API_Miel.Carrito.routes.CarritoRoutes;
import org.API_Miel.Direccion.routes.DireccionRoutes;
import org.API_Miel.MetodoPago.routes.MetodoPagoRoutes;
import org.API_Miel.Pedido.routes.PedidoRoutes;
import org.API_Miel.Producto.routes.ProductoRoutes;
import org.API_Miel.Resena.routes.ResenaRoutes;
import org.API_Miel.Usuario.routes.UsuarioRoutes;

public class ApiRouters {
    
    public static void register(Javalin app) {
        System.out.println("Registrando rutas...");

        // 1. USUARIO (Método de instancia)
        new UsuarioRoutes().register(app);

        // 2. PRODUCTO (Método estático - static)
        ProductoRoutes.register(app);

        // 3. CARRITO (Método estático - static)
        CarritoRoutes.register(app);

        // 4. PEDIDO (Método de instancia)
        new PedidoRoutes().register(app);

        // 5. DIRECCION (Método de instancia)
        new DireccionRoutes().register(app);

        // 6. METODO PAGO (Método de instancia)
        new MetodoPagoRoutes().register(app);

        // 7. RESENA (Método de instancia con nombre diferente: registerRoutes)
        new ResenaRoutes().registerRoutes(app);

        // Ruta base de prueba
        app.get("/", ctx -> ctx.result("API Miel E-commerce funcionando correctamente 🐝"));
        
        System.out.println("Todas las rutas registradas con éxito.");
    }
}