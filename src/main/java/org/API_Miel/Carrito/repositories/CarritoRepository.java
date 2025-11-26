package org.API_Miel.Carrito.repositories;

import org.API_Miel.Carrito.models.Carrito;
import org.API_Miel.Carrito.models.CarritoDetalle;
import org.API_Miel.Carrito.models.CarritoDetalleDTO;
import org.API_Miel.ConfigDB.DatabaseConfig;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarritoRepository {
    
    // ==================== CARRITO ====================
    
    public Optional<Carrito> findByUsuarioId(Long usuarioId) {
        String sql = "SELECT id, usuario_id, fecha_creacion, fecha_actualizacion " +
                     "FROM carrito WHERE usuario_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToCarrito(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar carrito por usuario", e);
        }
    }
    
    public Carrito save(Carrito carrito) {
        String sql = "INSERT INTO carrito (usuario_id) VALUES (?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, carrito.getUsuarioId());
            stmt.executeUpdate();
            
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                carrito.setId(generatedKeys.getLong(1));
            }
            
            return carrito;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear carrito", e);
        }
    }
    
    public Carrito getOrCreateCarrito(Long usuarioId) {
        Optional<Carrito> carritoExistente = findByUsuarioId(usuarioId);
        
        if (carritoExistente.isPresent()) {
            return carritoExistente.get();
        }
        
        Carrito nuevoCarrito = new Carrito();
        nuevoCarrito.setUsuarioId(usuarioId);
        return save(nuevoCarrito);
    }
    
    public void deleteByUsuarioId(Long usuarioId) {
        String sql = "DELETE FROM carrito WHERE usuario_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, usuarioId);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar carrito", e);
        }
    }
    
    // ==================== CARRITO DETALLE ====================
    
    public List<CarritoDetalleDTO> findDetallesByCarritoId(Long carritoId) {
        String sql = "SELECT cd.id, cd.carrito_id, cd.producto_id, cd.cantidad, cd.fecha_agregado, " +
                     "p.nombre, p.descripcion, p.precio, p.imagen_url " +
                     "FROM carrito_detalle cd " +
                     "INNER JOIN producto p ON cd.producto_id = p.id " +
                     "WHERE cd.carrito_id = ? AND p.activo = true " +
                     "ORDER BY cd.fecha_agregado DESC";
        
        List<CarritoDetalleDTO> detalles = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, carritoId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                detalles.add(mapResultSetToCarritoDetalleDTO(rs));
            }
            return detalles;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener detalles del carrito", e);
        }
    }
    
    public Optional<CarritoDetalle> findDetalleByCarritoAndProducto(Long carritoId, Long productoId) {
        String sql = "SELECT id, carrito_id, producto_id, cantidad, fecha_agregado " +
                     "FROM carrito_detalle WHERE carrito_id = ? AND producto_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, carritoId);
            stmt.setLong(2, productoId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToCarritoDetalle(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar detalle del carrito", e);
        }
    }
    
    public CarritoDetalle saveDetalle(CarritoDetalle detalle) {
        String sql = "INSERT INTO carrito_detalle (carrito_id, producto_id, cantidad) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, detalle.getCarritoId());
            stmt.setLong(2, detalle.getProductoId());
            stmt.setInt(3, detalle.getCantidad());
            
            stmt.executeUpdate();
            
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                detalle.setId(generatedKeys.getLong(1));
            }
            
            return detalle;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al agregar item al carrito", e);
        }
    }
    
    public void updateDetalleCantidad(Long detalleId, Integer cantidad) {
        String sql = "UPDATE carrito_detalle SET cantidad = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cantidad);
            stmt.setLong(2, detalleId);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar cantidad del item", e);
        }
    }
    
    public void deleteDetalle(Long detalleId) {
        String sql = "DELETE FROM carrito_detalle WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, detalleId);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar item del carrito", e);
        }
    }
    
    public void deleteDetallesByCarritoId(Long carritoId) {
        String sql = "DELETE FROM carrito_detalle WHERE carrito_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, carritoId);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al limpiar carrito", e);
        }
    }
    
    public Integer countItemsByCarritoId(Long carritoId) {
        String sql = "SELECT COALESCE(SUM(cantidad), 0) as total FROM carrito_detalle WHERE carrito_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, carritoId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar items del carrito", e);
        }
    }
    
    // ==================== MAPPERS ====================
    
    private Carrito mapResultSetToCarrito(ResultSet rs) throws SQLException {
        return new Carrito(
            rs.getLong("id"),
            rs.getLong("usuario_id"),
            rs.getTimestamp("fecha_creacion").toLocalDateTime(),
            rs.getTimestamp("fecha_actualizacion").toLocalDateTime()
        );
    }
    
    private CarritoDetalle mapResultSetToCarritoDetalle(ResultSet rs) throws SQLException {
        return new CarritoDetalle(
            rs.getLong("id"),
            rs.getLong("carrito_id"),
            rs.getLong("producto_id"),
            rs.getInt("cantidad"),
            rs.getTimestamp("fecha_agregado").toLocalDateTime()
        );
    }
    
    private CarritoDetalleDTO mapResultSetToCarritoDetalleDTO(ResultSet rs) throws SQLException {
        BigDecimal precio = rs.getBigDecimal("precio");
        Integer cantidad = rs.getInt("cantidad");
        BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(cantidad));
        
        return new CarritoDetalleDTO(
            rs.getLong("id"),
            rs.getLong("carrito_id"),
            rs.getLong("producto_id"),
            rs.getString("nombre"),
            rs.getString("descripcion"),
            precio,
            rs.getString("imagen_url"),
            cantidad,
            subtotal,
            rs.getTimestamp("fecha_agregado").toLocalDateTime()
        );
    }

    public void clearCarrito(Long carritoId) {
        String sql = "DELETE FROM carrito_detalle WHERE carrito_id = ?";
    
        try (Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, carritoId);
                stmt.executeUpdate();
        
        } catch (SQLException e) {
            throw new RuntimeException("Error al vaciar el carrito", e);
        }
    }
}