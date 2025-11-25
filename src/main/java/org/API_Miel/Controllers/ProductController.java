package org.API_Miel.Controllers;

import io.javalin.http.Context;

import java.util.Map;

import org.API_Miel.Models.Producto;
import org.API_Miel.Services.ProductService;

public class ProductController {
    private final ProductService service = new ProductService();

    public void list(Context ctx) {
        ctx.json(service.list());
    }

    public void get(Context ctx) {
        ctx.json(service.get(Long.parseLong(ctx.pathParam("id"))));
    }

    public void create(Context ctx) {
        ctx.json(service.create(ctx.bodyAsClass(Producto.class)));
    }

    public void update(Context ctx) {
        service.update(Long.parseLong(ctx.pathParam("id")), ctx.bodyAsClass(Producto.class));
        ctx.status(204);
    }

    public void delete(Context ctx) {
        service.delete(Long.parseLong(ctx.pathParam("id")));
        ctx.status(204);
    }

    public void listStockChart(Context ctx) {
        ctx.json(service.listStockChart());
    }
    // LO DE LA GRAFICA DE STOCK DE PRODUCTOS

    public void topSelling(Context ctx) {
        ctx.json(service.getTopSelling());
    }

    public void checkProduct(Context ctx) {
    try {
        long productId = Long.parseLong(ctx.pathParam("id"));
        var product = service.get(productId);
        
        if (product == null) {
            ctx.status(404).json(Map.of("exists", false, "message", "Producto no encontrado"));
        } else {
            ctx.json(Map.of(
                "exists", true,
                "product", Map.of(
                    "id", product.getIdProducto(),
                    "nombre", product.getNombre(),
                    "precio", product.getPrecio(),
                    "stock", product.getStock()
                )
            ));
        }
    } catch (Exception e) {
        ctx.status(400).json(Map.of("error", e.getMessage()));
    }
}
}
