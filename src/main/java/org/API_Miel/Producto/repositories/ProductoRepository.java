package org.API_Miel.Producto.repositories;

import org.API_Miel.ConfigDB.DatabaseConfig;
import org.API_Miel.Producto.models.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoRepository {
    
    public Optional<Producto> findById(Long id) {
        String sql = "SELECT id, nombre, descripcion, precio, stock, imagen_url, activo, " +
                     "fecha_creacion, fecha_actualizacion " +
                     "FROM producto WHERE id = ? AND activo = true";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToProducto(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar producto por ID", e);
        }
    }
    
    public List<Producto> findAll() {
        String sql = "SELECT id, nombre, descripcion, precio, stock, imagen_url, activo, " +
                     "fecha_creacion, fecha_actualizacion " +
                     "FROM producto WHERE activo = true ORDER BY fecha_creacion DESC";
        List<Producto> productos = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                productos.add(mapResultSetToProducto(rs));
            }
            return productos;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar productos", e);
        }
    }
    
    public List<Producto> findAllIncludingInactive() {
        String sql = "SELECT id, nombre, descripcion, precio, stock, imagen_url, activo, " +
                     "fecha_creacion, fecha_actualizacion " +
                     "FROM producto ORDER BY fecha_creacion DESC";
        List<Producto> productos = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                productos.add(mapResultSetToProducto(rs));
            }
            return productos;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todos los productos", e);
        }
    }
    
    public List<Producto> searchByNombre(String nombre) {
        String sql = "SELECT id, nombre, descripcion, precio, stock, imagen_url, activo, " +
                     "fecha_creacion, fecha_actualizacion " +
                     "FROM producto WHERE activo = true AND nombre LIKE ? ORDER BY nombre";
        List<Producto> productos = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                productos.add(mapResultSetToProducto(rs));
            }
            return productos;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar productos por nombre", e);
        }
    }
    
    public Producto save(Producto producto) {
        String sql = "INSERT INTO producto (nombre, descripcion, precio, stock, imagen_url) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setBigDecimal(3, producto.getPrecio());
            stmt.setInt(4, producto.getStock());
            stmt.setString(5, producto.getImagenUrl());
            
            stmt.executeUpdate();
            
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                producto.setId(generatedKeys.getLong(1));
            }
            
            return producto;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear producto", e);
        }
    }
    
    public void update(Producto producto) {
        String sql = "UPDATE producto SET nombre = ?, descripcion = ?, precio = ?, " +
                     "stock = ?, imagen_url = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setBigDecimal(3, producto.getPrecio());
            stmt.setInt(4, producto.getStock());
            stmt.setString(5, producto.getImagenUrl());
            stmt.setLong(6, producto.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Producto no encontrado");
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar producto", e);
        }
    }
    
    public void updateStock(Long id, Integer cantidad) {
        String sql = "UPDATE producto SET stock = stock + ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cantidad);
            stmt.setLong(2, id);
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar stock", e);
        }
    }
    
    public void delete(Long id) {
        String sql = "UPDATE producto SET activo = false WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("Producto no encontrado");
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar producto", e);
        }
    }
    
    private Producto mapResultSetToProducto(ResultSet rs) throws SQLException {
        return new Producto(
            rs.getLong("id"),
            rs.getString("nombre"),
            rs.getString("descripcion"),
            rs.getBigDecimal("precio"),
            rs.getInt("stock"),
            rs.getString("imagen_url"),
            rs.getBoolean("activo"),
            rs.getTimestamp("fecha_creacion").toLocalDateTime(),
            rs.getTimestamp("fecha_actualizacion").toLocalDateTime()
        );
    }
}