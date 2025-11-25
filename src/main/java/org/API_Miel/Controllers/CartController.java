package org.API_Miel.Controllers;

import io.javalin.http.Context;
import org.API_Miel.Services.CartService;

import java.util.Map;

public class CartController {
    private final CartService service = new CartService();

  public void addItem(Context ctx) {
    try {
        System.out.println("POST /api/cart/items - INICIANDO");
        
        Map<String, Object> body = ctx.bodyAsClass(Map.class);
        System.out.println(" Body recibido: " + body);
        
        // Log más detallado
        System.out.println("🔍 Keys en body: " + body.keySet());
        
        if (body.get("idUsuario") == null) throw new RuntimeException("Falta idUsuario");
        if (body.get("idProducto") == null) throw new RuntimeException("Falta idProducto");
        if (body.get("cantidad") == null) throw new RuntimeException("Falta cantidad");

        long idUsuario = ((Number) body.get("idUsuario")).longValue();
        long idProducto = ((Number) body.get("idProducto")).longValue();
        int cantidad = ((Number) body.get("cantidad")).intValue();
        
        System.out.println("📦 Datos procesados - User: " + idUsuario + ", Product: " + idProducto + ", Quantity: " + cantidad);
        
        // Verificar valores
        if (idProducto <= 0) {
            throw new RuntimeException("ID de producto inválido: " + idProducto);
        }
        if (idUsuario <= 0) {
            throw new RuntimeException("ID de usuario inválido: " + idUsuario);
        }
        
        service.addToCart(idUsuario, idProducto, cantidad);
        
        System.out.println("✅ Producto agregado exitosamente al carrito");
        ctx.status(204);
        
    } catch (Exception e) {
        System.out.println("❌ ERROR en addItem: " + e.getMessage());
        e.printStackTrace();
        ctx.status(400).json(Map.of("error", "Error al agregar al carrito: " + e.getMessage()));
    }
}

    // --------------------------------------------------
    // DELETE /api/cart/items/{idProducto} (Borrar Item)
    // --------------------------------------------------
    public void removeItem(Context ctx) {
        try {
           
            String idParam = ctx.pathParam("idProducto"); 
            
            if (idParam == null || !idParam.matches("\\d+")) {
                ctx.status(400).result("ID inválido: debe ser un número entero");
                return;
            }

            long idDetalle = Long.parseLong(idParam);
            System.out.println("🗑️ Eliminando detalle ID: " + idDetalle);
            
            service.removeItem(idDetalle);
            
            ctx.status(200).result("Item eliminado");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("Error interno al eliminar: " + e.getMessage());
        }
    }

    // --------------------------------------------------
    // DELETE /api/cart/clear (Vaciar Carrito)
    // --------------------------------------------------
    public void clear(Context ctx) {
        try {
            System.out.println(" DELETE /api/cart/clear - INICIANDO");
            
            long idUsuario = obtenerIdUsuario(ctx, null);
            System.out.println(" Vaciando carrito del usuario: " + idUsuario);
            
            service.clearCart(idUsuario);
            
            System.out.println(" Carrito vaciado");
            ctx.status(204);
            
        } catch (Exception e) {
            System.out.println(" ERROR en clear: " + e.getMessage());
            ctx.status(400).json(Map.of("error", e.getMessage()));
        }
    }

    // --------------------------------------------------
    // GET /api/cart/items (Obtener Carrito)
    // --------------------------------------------------
    public void getItems(Context ctx) {
        try {
            System.out.println(" GET /api/cart/items - INICIANDO");
            
            long idUsuario = obtenerIdUsuario(ctx, null);
            System.out.println(" Consultando usuario: " + idUsuario);
            
            var items = service.getCart(idUsuario);
            
            System.out.println(" Items encontrados: " + items.size());
            ctx.json(items);
            
        } catch (Exception e) {
            System.out.println(" ERROR en getItems: " + e.getMessage());
            e.printStackTrace();
            ctx.status(400).json(Map.of("error", e.getMessage()));
        }
    }

    private long obtenerIdUsuario(Context ctx, Map<String, Object> body) {
        String queryUserId = ctx.queryParam("idUsuario");
        if (queryUserId != null && !queryUserId.isEmpty()) return Long.parseLong(queryUserId);
        
        
        return 1L; 
    }
}