package org.API_Miel.Producto.services;

import org.API_Miel.Producto.models.CreateProductoRequest;
import org.API_Miel.Producto.models.UpdateProductoRequest;
import org.API_Miel.Producto.models.Producto;
import org.API_Miel.Producto.repositories.ProductoRepository;
import java.util.List;
import java.util.Optional;

public class ProductoService {
    private final ProductoRepository repository;
    
    public ProductoService() {
        this.repository = new ProductoRepository();
    }
    
    public Producto createProducto(CreateProductoRequest request) {
        // Validaciones
        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es requerido");
        }
        
        if (request.getPrecio() == null || request.getPrecio().doubleValue() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        
        if (request.getStock() == null || request.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        
        // Crear producto
        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setImagenUrl(request.getImagenUrl());
        producto.setActivo(true);
        
        return repository.save(producto);
    }
    
    public Optional<Producto> getProductoById(Long id) {
        return repository.findById(id);
    }
    
    public List<Producto> getAllProductos() {
        return repository.findAll();
    }
    
    public List<Producto> getAllProductosIncludingInactive() {
        return repository.findAllIncludingInactive();
    }
    
    public List<Producto> searchProductos(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return repository.findAll();
        }
        return repository.searchByNombre(nombre);
    }
    
    public Producto updateProducto(Long id, UpdateProductoRequest request) {
        Producto producto = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        
        // Validaciones
        if (request.getNombre() != null && !request.getNombre().trim().isEmpty()) {
            producto.setNombre(request.getNombre());
        }
        
        if (request.getDescripcion() != null) {
            producto.setDescripcion(request.getDescripcion());
        }
        
        if (request.getPrecio() != null) {
            if (request.getPrecio().doubleValue() <= 0) {
                throw new IllegalArgumentException("El precio debe ser mayor a 0");
            }
            producto.setPrecio(request.getPrecio());
        }
        
        if (request.getStock() != null) {
            if (request.getStock() < 0) {
                throw new IllegalArgumentException("El stock no puede ser negativo");
            }
            producto.setStock(request.getStock());
        }
        
        if (request.getImagenUrl() != null) {
            producto.setImagenUrl(request.getImagenUrl());
        }
        
        repository.update(producto);
        return producto;
    }
    
    public void deleteProducto(Long id) {
        if (!repository.findById(id).isPresent()) {
            throw new IllegalArgumentException("Producto no encontrado");
        }
        repository.delete(id);
    }
    
    public void updateStock(Long id, Integer cantidad) {
        Producto producto = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        
        int nuevoStock = producto.getStock() + cantidad;
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("Stock insuficiente");
        }
        
        repository.updateStock(id, cantidad);
    }
}