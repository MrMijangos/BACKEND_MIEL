package org.API_Miel.Usuario.repositories;

import org.API_Miel.ConfigDB.DatabaseConfig;
import org.API_Miel.Usuario.models.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository {
    
    public Optional<Usuario> findById(Long id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToUsuario(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por ID", e);
        }
    }
    
    public Optional<Usuario> findByCorreo(String correo) {
        String sql = "SELECT * FROM usuario WHERE correo = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToUsuario(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por correo", e);
        }
    }
    
    public Usuario save(Usuario usuario) {
        String sql = "INSERT INTO usuario (nombre_completo, correo, contrasena, num_celular, rol_id) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, usuario.getNombreCompleto());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, usuario.getContrasena());
            stmt.setString(4, usuario.getNumCelular());
            stmt.setInt(5, usuario.getRolId() != null ? usuario.getRolId() : 2);
            
            stmt.executeUpdate();
            
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                usuario.setId(generatedKeys.getLong(1));
            }
            
            return usuario;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar usuario", e);
        }
    }
    
    public Usuario update(Usuario usuario) {
        String sql = "UPDATE usuario SET nombre_completo = ?, num_celular = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNombreCompleto());
            stmt.setString(2, usuario.getNumCelular());
            stmt.setLong(3, usuario.getId());
            
            stmt.executeUpdate();
            return usuario;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario", e);
        }
    }
    
    public void updateUltimaConexion(Long id) {
        String sql = "UPDATE usuario SET ultima_conexion = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar última conexión", e);
        }
    }
    
    public List<Usuario> findAll() {
        String sql = "SELECT * FROM usuario ORDER BY fecha_registro DESC";
        List<Usuario> usuarios = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
            return usuarios;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usuarios", e);
        }
    }
    
    public void delete(Long id) {
        String sql = "UPDATE usuario SET activo = FALSE WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al desactivar usuario", e);
        }
    }
    
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id"));
        usuario.setNombreCompleto(rs.getString("nombre_completo"));
        usuario.setCorreo(rs.getString("correo"));
        usuario.setContrasena(rs.getString("contrasena"));
        usuario.setNumCelular(rs.getString("num_celular"));
        usuario.setRolId(rs.getInt("rol_id"));
        usuario.setActivo(rs.getBoolean("activo"));
        usuario.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        usuario.setUltimaConexion(rs.getTimestamp("ultima_conexion"));
        return usuario;
    }
}