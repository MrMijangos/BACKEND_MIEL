package org.API_Miel.Carrito.services;

import org.API_Miel.Carrito.models.Carrito;
import org.API_Miel.Carrito.models.CarritoDetalle;
import org.API_Miel.Carrito.models.CarritoDetalleDTO;
import org.API_Miel.Carrito.models.CarritoResponse;
import org.API_Miel.Carrito.models.AddItemRequest;
import org.API_Miel.Carrito.models.UpdateItemRequest;
import org.API_Miel.Carrito.repositories.CarritoRepository;
import org.API_Miel.Producto.models.Producto;
import org.API_Miel.Producto.repositories.ProductoRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class CarritoService {
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;
    
    public CarritoService() {
        this.carritoRepository = new CarritoRepository();
        this.productoRepository = new ProductoRepository();
    }
    
    public CarritoResponse getCarritoByUsuario(Long usuarioId) {
        Carrito carrito = carritoRepository.getOrCreateCarrito(usuarioId);
        List<CarritoDetalleDTO> items = carritoRepository.findDetallesByCarritoId(carrito.getId());
        
        Integer totalItems = carritoRepository.countItemsByCarritoId(carrito.getId());
        BigDecimal totalGeneral = calcularTotalGeneral(items);
        
        return new CarritoResponse(carrito.getId(), usuarioId, items, totalItems, totalGeneral);
    }
    
    public CarritoResponse addItem(Long usuarioId, AddItemRequest request) {
        // Validaciones
        if (request.getProductoId() == null) {
            throw new IllegalArgumentException("El ID del producto es requerido");
        }
        
        if (request.getCantidad() == null || request.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        
        // Verificar que el producto existe y está activo
        Producto producto = productoRepository.findById(request.getProductoId())
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        
        // Verificar stock disponible
        if (producto.getStock() < request.getCantidad()) {
            throw new IllegalArgumentException("Stock insuficiente. Disponible: " + producto.getStock());
        }
        
        // Obtener o crear carrito
        Carrito carrito = carritoRepository.getOrCreateCarrito(usuarioId);
        
        // Verificar si el producto ya está en el carrito
        Optional<CarritoDetalle> detalleExistente = carritoRepository
            .findDetalleByCarritoAndProducto(carrito.getId(), request.getProductoId());
        
        if (detalleExistente.isPresent()) {
            // Actualizar cantidad
            CarritoDetalle detalle = detalleExistente.get();
            Integer nuevaCantidad = detalle.getCantidad() + request.getCantidad();
            
            if (producto.getStock() < nuevaCantidad) {
                throw new IllegalArgumentException("Stock insuficiente. Disponible: " + producto.getStock());
            }
            
            carritoRepository.updateDetalleCantidad(detalle.getId(), nuevaCantidad);
        } else {
            // Agregar nuevo item
            CarritoDetalle nuevoDetalle = new CarritoDetalle();
            nuevoDetalle.setCarritoId(carrito.getId());
            nuevoDetalle.setProductoId(request.getProductoId());
            nuevoDetalle.setCantidad(request.getCantidad());
            
            carritoRepository.saveDetalle(nuevoDetalle);
        }
        
        return getCarritoByUsuario(usuarioId);
    }
    
    public CarritoResponse updateItem(Long usuarioId, Long productoId, UpdateItemRequest request) {
        if (request.getCantidad() == null || request.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        
        Carrito carrito = carritoRepository.getOrCreateCarrito(usuarioId);
        
        CarritoDetalle detalle = carritoRepository
            .findDetalleByCarritoAndProducto(carrito.getId(), productoId)
            .orElseThrow(() -> new IllegalArgumentException("Item no encontrado en el carrito"));
        
        // Verificar stock
        Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        
        if (producto.getStock() < request.getCantidad()) {
            throw new IllegalArgumentException("Stock insuficiente. Disponible: " + producto.getStock());
        }
        
        carritoRepository.updateDetalleCantidad(detalle.getId(), request.getCantidad());
        
        return getCarritoByUsuario(usuarioId);
    }
    
    public CarritoResponse removeItem(Long usuarioId, Long productoId) {
        Carrito carrito = carritoRepository.getOrCreateCarrito(usuarioId);
        
        CarritoDetalle detalle = carritoRepository
            .findDetalleByCarritoAndProducto(carrito.getId(), productoId)
            .orElseThrow(() -> new IllegalArgumentException("Item no encontrado en el carrito"));
        
        carritoRepository.deleteDetalle(detalle.getId());
        
        return getCarritoByUsuario(usuarioId);
    }
    
    public void clearCarrito(Long usuarioId) {
        Carrito carrito = carritoRepository.getOrCreateCarrito(usuarioId);
        carritoRepository.deleteDetallesByCarritoId(carrito.getId());
    }
    
    private BigDecimal calcularTotalGeneral(List<CarritoDetalleDTO> items) {
        return items.stream()
            .map(CarritoDetalleDTO::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}