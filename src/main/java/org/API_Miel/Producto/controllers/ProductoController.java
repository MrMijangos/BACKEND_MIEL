package org.API_Miel.Producto.controllers;

import org.API_Miel.Producto.models.CreateProductoRequest;
import org.API_Miel.Producto.models.UpdateProductoRequest;
import org.API_Miel.Producto.models.Producto;
import org.API_Miel.Producto.services.ProductoService;
import io.javalin.http.Context;
import java.util.List;
import java.util.Optional;

public class ProductoController {
    private final ProductoService service;
    
    public ProductoController() {
        this.service = new ProductoService();
    }
    
    public void create(Context ctx) {
        try {
            CreateProductoRequest request = ctx.bodyAsClass(CreateProductoRequest.class);
            Producto producto = service.createProducto(request);
            ctx.status(201).json(producto);
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            ctx.status(500).json(new ErrorResponse("Error al crear producto: " + e.getMessage()));
        }
    }
    
    public void getById(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            Optional<Producto> producto = service.getProductoById(id);
            
            if (producto.isPresent()) {
                ctx.json(producto.get());
            } else {
                ctx.status(404).json(new ErrorResponse("Producto no encontrado"));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ErrorResponse("ID inválido"));
        }
    }
    
    public void getAll(Context ctx) {
        try {
            String includeInactive = ctx.queryParam("includeInactive");
            List<Producto> productos;
            
            if ("true".equals(includeInactive)) {
                productos = service.getAllProductosIncludingInactive();
            } else {
                productos = service.getAllProductos();
            }
            
            ctx.json(productos);
        } catch (Exception e) {
            ctx.status(500).json(new ErrorResponse("Error al obtener productos"));
        }
    }
    
    public void search(Context ctx) {
        try {
            String nombre = ctx.queryParam("nombre");
            List<Producto> productos = service.searchProductos(nombre);
            ctx.json(productos);
        } catch (Exception e) {
            ctx.status(500).json(new ErrorResponse("Error al buscar productos"));
        }
    }
    
    public void update(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            UpdateProductoRequest request = ctx.bodyAsClass(UpdateProductoRequest.class);
            Producto producto = service.updateProducto(id, request);
            ctx.json(producto);
        } catch (IllegalArgumentException e) {
            ctx.status(404).json(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            ctx.status(500).json(new ErrorResponse("Error al actualizar producto"));
        }
    }
    
    public void delete(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            service.deleteProducto(id);
            ctx.status(204);
        } catch (IllegalArgumentException e) {
            ctx.status(404).json(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            ctx.status(500).json(new ErrorResponse("Error al eliminar producto"));
        }
    }
    
    public void updateStock(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            UpdateStockRequest request = ctx.bodyAsClass(UpdateStockRequest.class);
            service.updateStock(id, request.cantidad());
            ctx.status(200).json(new SuccessResponse("Stock actualizado correctamente"));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            ctx.status(500).json(new ErrorResponse("Error al actualizar stock"));
        }
    }
    
    record ErrorResponse(String error) {}
    record SuccessResponse(String message) {}
    record UpdateStockRequest(Integer cantidad) {}
}