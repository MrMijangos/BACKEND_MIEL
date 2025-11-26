package org.API_Miel.Resena.repositories;

import org.API_Miel.ConfigDB.DatabaseConfig;
import org.API_Miel.Resena.models.Resena;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResenaRepository {

    public void crear(Resena resena) throws SQLException {
        String sql = "INSERT INTO resena (producto_id, usuario_id, calificacion, comentario) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, resena.getProductoId());
            stmt.setLong(2, resena.getUsuarioId());
            stmt.setInt(3, resena.getCalificacion());
            stmt.setString(4, resena.getComentario());
            
            stmt.executeUpdate();
        }
    }

    public List<Resena> buscarPorProducto(Long productoId) throws SQLException {
        List<Resena> lista = new ArrayList<>();
        String sql = "SELECT * FROM resena WHERE producto_id = ? AND activa = TRUE ORDER BY fecha_creacion DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, productoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSet(rs));
                }
            }
        }
        return lista;
    }

    // Mapeo auxiliar
    private Resena mapResultSet(ResultSet rs) throws SQLException {
        return new Resena(
            rs.getLong("id"),
            rs.getLong("producto_id"),
            rs.getLong("usuario_id"),
            rs.getInt("calificacion"),
            rs.getString("comentario"),
            rs.getBoolean("activa"),
            rs.getTimestamp("fecha_creacion")
        );
    }
}