package org.API_Miel.Controllers;

import io.javalin.http.Context;
import org.API_Miel.Models.Direccion;
import org.API_Miel.Services.ShippingService;

public class ShippingController {
    private final ShippingService service = new ShippingService();

    public void list(Context ctx) {
        long idUsuario = Long.parseLong(ctx.queryParam("idUsuario"));
        ctx.json(service.listar(idUsuario));
    }

    public void create(Context ctx) {
        Direccion d = ctx.bodyAsClass(Direccion.class);
        ctx.json(service.crear(d));
    }

    public void delete(Context ctx) {
        try {
            long id = Long.parseLong(ctx.pathParam("id"));
            boolean ok = service.eliminar(id);
            if (ok) { ctx.status(204); } else { ctx.status(404).json(java.util.Map.of("error", "No encontrado")); }
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("en uso")) {
                ctx.status(409).json(java.util.Map.of("error", e.getMessage()));
            } else {
                ctx.status(500).json(java.util.Map.of("error", e.getMessage()));
            }
        }
    }
}
