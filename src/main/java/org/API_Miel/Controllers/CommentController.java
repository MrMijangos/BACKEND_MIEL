package org.API_Miel.Controllers;

import io.javalin.http.Context;
import org.API_Miel.Models.Resena;
import org.API_Miel.Services.CommentService;

import java.util.Map;

public class CommentController {
    private final CommentService service = new CommentService();

    public void listByProduct(Context ctx) {
        long idProducto = Long.parseLong(ctx.pathParam("idProducto"));
        ctx.json(service.listarPorProducto(idProducto));
    }

    public void create(Context ctx) {
        Map<String, Object> body = ctx.bodyAsClass(Map.class);
        Resena r = new Resena();
        r.setIdProducto(((Number) body.get("idProducto")).longValue());
        r.setIdUsuario(((Number) body.get("idUsuario")).longValue());
        r.setCalificacion(((Number) body.get("calificacion")).intValue());
        r.setComentario((String) body.get("comentario"));
        ctx.json(service.crear(r));
    }

    public void delete(Context ctx) {
        long idResena = Long.parseLong(ctx.pathParam("idResena"));
        service.eliminar(idResena);
        ctx.json(Map.of("message", "Comentario eliminado exitosamente"));
    }
}