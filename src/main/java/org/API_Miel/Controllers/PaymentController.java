package org.API_Miel.Controllers;

import io.javalin.http.Context;
import org.API_Miel.Models.MetodoPago;
import org.API_Miel.Services.PaymentService;

import java.util.Map;

public class PaymentController {
    private final PaymentService service = new PaymentService();

    public void list(Context ctx) {
        try {
            System.out.println("=== 💳 GET /api/payments - INICIANDO ===");
            
            String idUsuarioParam = ctx.queryParam("idUsuario");
            System.out.println("🔍 Parámetro idUsuario recibido: " + idUsuarioParam);
            
            if (idUsuarioParam == null) {
                ctx.status(400).json(Map.of("error", "Falta el parámetro idUsuario"));
                return;
            }
            
            long idUsuario = Long.parseLong(idUsuarioParam);
            System.out.println("👤 Consultando métodos de pago para usuario: " + idUsuario);
            
            var methods = service.listar(idUsuario);
            System.out.println("✅ Métodos encontrados: " + methods.size());
            
            ctx.json(methods);
            
        } catch (Exception e) {
            System.out.println("❌ ERROR en list: " + e.getMessage());
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }

    public void create(Context ctx) {
        try {
            System.out.println("=== 💳 POST /api/payments - INICIANDO ===");
            
            MetodoPago m = ctx.bodyAsClass(MetodoPago.class);
            System.out.println("📦 MétodoPago recibido: " + m);
            System.out.println("🔍 ID_Usuario: " + m.getIdUsuario());
            System.out.println("🔍 Tipo: " + m.getTipo());
            System.out.println("🔍 Detalles: " + m.getDetalles());
            
            long id = service.crear(m);
            System.out.println("✅ Método de pago creado con ID: " + id);
            
            ctx.json(Map.of("id", id, "message", "Método de pago creado exitosamente"));
            
        } catch (Exception e) {
            System.out.println("❌ ERROR en create: " + e.getMessage());
            e.printStackTrace();
            ctx.status(400).json(Map.of("error", e.getMessage()));
        }
    }

    public void delete(Context ctx) {
        try {
            System.out.println("=== 💳 DELETE /api/payments/{id} - INICIANDO ===");
            
            long id = Long.parseLong(ctx.pathParam("id"));
            System.out.println("🗑️ Eliminando método de pago ID: " + id);
            
            boolean ok = service.eliminar(id);
            if (ok) { 
                System.out.println("✅ Método de pago eliminado");
                ctx.status(204); 
            } else { 
                System.out.println("❌ Método de pago no encontrado");
                ctx.status(404).json(Map.of("error", "No encontrado")); 
            }
            
        } catch (RuntimeException e) {
            System.out.println("❌ ERROR en delete: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("en uso")) {
                ctx.status(409).json(Map.of("error", e.getMessage()));
            } else {
                ctx.status(500).json(Map.of("error", e.getMessage()));
            }
        } catch (Exception e) {
            System.out.println("❌ ERROR inesperado en delete: " + e.getMessage());
            ctx.status(500).json(Map.of("error", e.getMessage()));
        }
    }
}