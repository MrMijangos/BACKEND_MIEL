package org.API_Miel.Pedido.controllers;

import org.API_Miel.Pedido.models.CreatePedidoRequest;
import org.API_Miel.Pedido.services.PedidoService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController() {
        this.pedidoService = new PedidoService();
    }

    public void create(Context ctx) {
        try {
            Long usuarioId;
            try {
                usuarioId = Long.parseLong(ctx.pathParam("usuarioId"));
            } catch (NumberFormatException e) {
                ctx.status(HttpStatus.BAD_REQUEST).json(Map.of("message", "ID de usuario inválido"));
                return;
            }

            CreatePedidoRequest request = ctx.bodyAsClass(CreatePedidoRequest.class);
            
            if (request.getDireccionId() == null || request.getMetodoPagoId() == null) {
                ctx.status(HttpStatus.BAD_REQUEST).json(Map.of("message", "Faltan datos de dirección o método de pago"));
                return;
            }

            var pedido = pedidoService.createFromCarrito(usuarioId, request);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", pedido);
            result.put("message", "Pedido creado exitosamente");

            ctx.status(HttpStatus.CREATED).json(result);
        } catch (Exception e) {
            e.printStackTrace(); // Útil para depurar en consola
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            ctx.status(HttpStatus.BAD_REQUEST).json(error);
        }
    }

    public void getById(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            var pedido = pedidoService.getById(id);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", pedido);

            ctx.status(HttpStatus.OK).json(result);
        } catch (Exception e) {
            ctx.status(HttpStatus.NOT_FOUND).json(Map.of("success", false, "message", e.getMessage()));
        }
    }

    public void getByUsuario(Context ctx) {
        try {
            Long usuarioId = Long.parseLong(ctx.pathParam("usuarioId"));
            var pedidos = pedidoService.getByUsuarioId(usuarioId);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", pedidos);

            ctx.status(HttpStatus.OK).json(result);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(Map.of("success", false, "message", e.getMessage()));
        }
    }

    public void getAll(Context ctx) {
        try {
            var pedidos = pedidoService.getAll();
            ctx.json(Map.of("success", true, "data", pedidos));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(Map.of("success", false, "message", e.getMessage()));
        }
    }

    public void updateEstado(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            String estado = ctx.queryParam("estado");

            if (estado == null || estado.trim().isEmpty()) {
                throw new IllegalArgumentException("El parámetro 'estado' es requerido");
            }

            pedidoService.updateEstado(id, estado);

            ctx.json(Map.of("success", true, "message", "Estado actualizado exitosamente"));
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(Map.of("success", false, "message", e.getMessage()));
        }
    }
}