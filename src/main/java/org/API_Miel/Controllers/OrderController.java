package org.API_Miel.Controllers;

import io.javalin.http.Context;
import org.API_Miel.Services.OrderService;

import java.util.Map;

public class OrderController {
    private final OrderService service = new OrderService();

    private record EstadoRequest(String estado) {}
    
    private record ErrorResponse(String message) {}

   public void create(Context ctx) {
        try {
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            System.out.println("Create Order Body: " + body); // DEBUG

            if (body.get("idUsuario") == null) throw new RuntimeException("Falta idUsuario");
            if (body.get("idMetodoPago") == null) throw new RuntimeException("Falta idMetodoPago");
            if (body.get("idDireccion") == null) throw new RuntimeException("Falta idDireccion");

            long idUsuario = ((Number) body.get("idUsuario")).longValue();
            long idMetodoPago = ((Number) body.get("idMetodoPago")).longValue();
            long idDireccion = ((Number) body.get("idDireccion")).longValue();
            
            long id = service.crear(idUsuario, idMetodoPago, idDireccion);
            ctx.json(Map.of("idPedido", id));
            
        } catch (Exception e) {
            System.err.println(" ERROR EN CREATE ORDER: " + e.getMessage());
            e.printStackTrace();
            ctx.status(400).json(Map.of("error", e.getMessage()));
        }
    }
    
    public void list(Context ctx) {
        try {
            String userIdParam = ctx.queryParam("idUsuario");
            
            System.out.println("Recibida petición GET /api/orders");
            System.out.println("Parametro idUsuario: " + userIdParam);

            if (userIdParam != null && !userIdParam.isEmpty()) {
                long idUsuario = Long.parseLong(userIdParam);
                ctx.json(service.listar(idUsuario));
            } else {
                System.out.println("Ejecutando listarTodos() para admin...");
                var lista = service.listarTodos();
                System.out.println("Pedidos encontrados: " + lista.size());
                ctx.json(lista);
            }
        } catch (Exception e) {
            System.err.println(" ERROR GRAVE EN ORDER CONTROLLER (list) 🔥🔥🔥");
            System.err.println("Mensaje: " + e.getMessage());
            e.printStackTrace(); 
            
            ctx.status(500).json(Map.of("error", e.getMessage()));
        }
    }

    public void updateStatus(Context ctx) {
        try {
            long orderId = Long.parseLong(ctx.pathParam("id"));
            
            String nuevoEstado = ctx.bodyAsClass(EstadoRequest.class).estado();

            service.updateOrderStatus(orderId, nuevoEstado);
            
            ctx.status(204); 
            
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ErrorResponse("ID de pedido no válido."));
        } catch (Exception e) {
            System.err.println("ERROR EN UPDATE ORDER STATUS: " + e.getMessage());
            e.printStackTrace();
            ctx.status(500).json(new ErrorResponse("Error al actualizar el estado del pedido: " + e.getMessage()));
        }
    }
}