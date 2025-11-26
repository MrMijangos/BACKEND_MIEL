package org.API_Miel.Direccion.repositories;

import org.API_Miel.ConfigDB.DatabaseConfig;
import org.API_Miel.Direccion.models.Direccion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DireccionRepository {
    
    public Direccion save(Direccion direccion) {
        String sql = "INSERT INTO direccion (usuario_id, nombre_destinatario, telefono_contacto, " +
                     "calle, numero_exterior, numero_interior, colonia, codigo_postal, ciudad, estado, " +
                     "referencias, es_predeterminada) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, direccion.getUsuarioId());
            stmt.setString(2, direccion.getNombreDestinatario());
            stmt.setString(3, direccion.getTelefonoContacto());
            stmt.setString(4, direccion.getCalle());
            stmt.setString(5, direccion.getNumeroExterior());
            stmt.setString(6, direccion.getNumeroInterior());
            stmt.setString(7, direccion.getColonia());
            stmt.setString(8, direccion.getCodigoPostal());
            stmt.setString(9, direccion.getCiudad());
            stmt.setString(10, direccion.getEstado());
            stmt.setString(11, direccion.getReferencias());
            stmt.setBoolean(12, direccion.getEsPredeterminada() != null ? direccion.getEsPredeterminada() : false);
            
            stmt.executeUpdate();
            
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                direccion.setId(generatedKeys.getLong(1));
            }
            
            return direccion;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar dirección", e);
        }
    }
    
    public List<Direccion> findByUsuarioId(Long usuarioId) {
        String sql = "SELECT * FROM direccion WHERE usuario_id = ? AND activa = TRUE ORDER BY es_predeterminada DESC, fecha_creacion DESC";
        List<Direccion> direcciones = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                direcciones.add(mapResultSetToDireccion(rs));
            }
            return direcciones;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar direcciones del usuario", e);
        }
    }
    
    public Optional<Direccion> findById(Long id) {
        String sql = "SELECT * FROM direccion WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToDireccion(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar dirección", e);
        }
    }
    
    public void unsetPredeterminada(Long usuarioId) {
        String sql = "UPDATE direccion SET es_predeterminada = FALSE WHERE usuario_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, usuarioId);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar direcciones", e);
        }
    }
    
    public Direccion update(Direccion direccion) {
        String sql = "UPDATE direccion SET nombre_destinatario = ?, telefono_contacto = ?, " +
                     "calle = ?, numero_exterior = ?, numero_interior = ?, colonia = ?, " +
                     "codigo_postal = ?, ciudad = ?, estado = ?, referencias = ?, " +
                     "es_predeterminada = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, direccion.getNombreDestinatario());
            stmt.setString(2, direccion.getTelefonoContacto());
            stmt.setString(3, direccion.getCalle());
            stmt.setString(4, direccion.getNumeroExterior());
            stmt.setString(5, direccion.getNumeroInterior());
            stmt.setString(6, direccion.getColonia());
            stmt.setString(7, direccion.getCodigoPostal());
            stmt.setString(8, direccion.getCiudad());
            stmt.setString(9, direccion.getEstado());
            stmt.setString(10, direccion.getReferencias());
            stmt.setBoolean(11, direccion.getEsPredeterminada());
            stmt.setLong(12, direccion.getId());
            
            stmt.executeUpdate();
            return direccion;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar dirección", e);
        }
    }
    
    public void delete(Long id) {
        String sql = "UPDATE direccion SET activa = FALSE WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar dirección", e);
        }
    }
    
    private Direccion mapResultSetToDireccion(ResultSet rs) throws SQLException {
        Direccion direccion = new Direccion();
        direccion.setId(rs.getLong("id"));
        direccion.setUsuarioId(rs.getLong("usuario_id"));
        direccion.setNombreDestinatario(rs.getString("nombre_destinatario"));
        direccion.setTelefonoContacto(rs.getString("telefono_contacto"));
        direccion.setCalle(rs.getString("calle"));
        direccion.setNumeroExterior(rs.getString("numero_exterior"));
        direccion.setNumeroInterior(rs.getString("numero_interior"));
        direccion.setColonia(rs.getString("colonia"));
        direccion.setCodigoPostal(rs.getString("codigo_postal"));
        direccion.setCiudad(rs.getString("ciudad"));
        direccion.setEstado(rs.getString("estado"));
        direccion.setReferencias(rs.getString("referencias"));
        direccion.setEsPredeterminada(rs.getBoolean("es_predeterminada"));
        direccion.setActiva(rs.getBoolean("activa"));
        direccion.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        direccion.setFechaActualizacion(rs.getTimestamp("fecha_actualizacion"));
        return direccion;
    }
}