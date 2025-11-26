package org.API_Miel.MetodoPago.repositories;

import org.API_Miel.ConfigDB.DatabaseConfig;
import org.API_Miel.MetodoPago.models.MetodoPago;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MetodoPagoRepository {
    
    public MetodoPago save(MetodoPago metodoPago) {
        String sql = "INSERT INTO metodo_pago (usuario_id, tipo_tarjeta, nombre_titular, " +
                     "ultimos_digitos, mes_expiracion, anio_expiracion, es_predeterminado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, metodoPago.getUsuarioId());
            stmt.setString(2, metodoPago.getTipoTarjeta());
            stmt.setString(3, metodoPago.getNombreTitular());
            stmt.setString(4, metodoPago.getUltimosDigitos());
            stmt.setInt(5, metodoPago.getMesExpiracion());
            stmt.setInt(6, metodoPago.getAnioExpiracion());
            stmt.setBoolean(7, metodoPago.getEsPredeterminado() != null ? metodoPago.getEsPredeterminado() : false);
            
            stmt.executeUpdate();
            
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                metodoPago.setId(generatedKeys.getLong(1));
            }
            
            return metodoPago;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar método de pago", e);
        }
    }
    
    public List<MetodoPago> findByUsuarioId(Long usuarioId) {
        String sql = "SELECT * FROM metodo_pago WHERE usuario_id = ? AND activo = TRUE " +
                     "ORDER BY es_predeterminado DESC, fecha_creacion DESC";
        List<MetodoPago> metodosPago = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                metodosPago.add(mapResultSetToMetodoPago(rs));
            }
            return metodosPago;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar métodos de pago del usuario", e);
        }
    }
    
    public Optional<MetodoPago> findById(Long id) {
        String sql = "SELECT * FROM metodo_pago WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToMetodoPago(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar método de pago", e);
        }
    }
    
    public void unsetPredeterminado(Long usuarioId) {
        String sql = "UPDATE metodo_pago SET es_predeterminado = FALSE WHERE usuario_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, usuarioId);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar métodos de pago", e);
        }
    }
    
    public void delete(Long id) {
        String sql = "UPDATE metodo_pago SET activo = FALSE WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar método de pago", e);
        }
    }
    
    private MetodoPago mapResultSetToMetodoPago(ResultSet rs) throws SQLException {
        MetodoPago metodoPago = new MetodoPago();
        metodoPago.setId(rs.getLong("id"));
        metodoPago.setUsuarioId(rs.getLong("usuario_id"));
        metodoPago.setTipoTarjeta(rs.getString("tipo_tarjeta"));
        metodoPago.setNombreTitular(rs.getString("nombre_titular"));
        metodoPago.setUltimosDigitos(rs.getString("ultimos_digitos"));
        metodoPago.setMesExpiracion(rs.getInt("mes_expiracion"));
        metodoPago.setAnioExpiracion(rs.getInt("anio_expiracion"));
        metodoPago.setEsPredeterminado(rs.getBoolean("es_predeterminado"));
        metodoPago.setActivo(rs.getBoolean("activo"));
        metodoPago.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        metodoPago.setFechaActualizacion(rs.getTimestamp("fecha_actualizacion"));
        return metodoPago;
    }
}