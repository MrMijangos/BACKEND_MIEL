package org.API_Miel.Repositories;

import org.API_Miel.ConfigDB.ConfigDB;
import org.API_Miel.Models.MetodoPago;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetodoPagoRepository {
    private final DataSource ds = ConfigDB.getDataSource();

    public List<MetodoPago> listByUsuario(long idUsuario) {
        List<MetodoPago> list = new ArrayList<>();
        try (Connection c = ds.getConnection();
                PreparedStatement ps = c.prepareStatement(
                        "SELECT ID_Metodo_Pago, ID_Usuario, Tipo, Detalles FROM Metodo_Pago WHERE ID_Usuario=?")) {
            ps.setLong(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            
            System.out.println("🔍 Ejecutando query para usuario: " + idUsuario);
            
            while (rs.next()) {
                MetodoPago metodo = map(rs);
                list.add(metodo);
                System.out.println("✅ Método encontrado: " + metodo);
            }
            
            System.out.println("📦 Total métodos encontrados: " + list.size());
            return list;
        } catch (SQLException e) {
            System.out.println("❌ Error en listByUsuario: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public long create(MetodoPago m) {
        try (Connection c = ds.getConnection();
                PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO Metodo_Pago(ID_Usuario,Tipo,Detalles) VALUES(?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, m.getIdUsuario());
            ps.setString(2, m.getTipo());
            ps.setString(3, m.getDetalles());
            
            System.out.println("💾 Insertando método de pago: User=" + m.getIdUsuario() + ", Tipo=" + m.getTipo());
            
            int affectedRows = ps.executeUpdate();
            System.out.println("✅ Filas afectadas: " + affectedRows);
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                long id = rs.getLong(1);
                System.out.println("🎯 ID generado: " + id);
                return id;
            }
            return 0;
        } catch (SQLException e) {
            System.out.println("❌ Error en create: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean delete(long idMetodoPago) {
        try (Connection c = ds.getConnection()) {
            System.out.println("🔍 Verificando si el método está en uso: " + idMetodoPago);
            
            try (PreparedStatement chk = c.prepareStatement("SELECT 1 FROM Pedido WHERE ID_Metodo_Pago=? LIMIT 1")) {
                chk.setLong(1, idMetodoPago);
                ResultSet rs = chk.executeQuery();
                if (rs.next()) {
                    System.out.println("❌ Método de pago en uso, no se puede eliminar");
                    throw new RuntimeException("Metodo de pago en uso");
                }
            }
            
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM Metodo_Pago WHERE ID_Metodo_Pago=?")) {
                ps.setLong(1, idMetodoPago);
                int n = ps.executeUpdate();
                System.out.println("🗑️ Filas eliminadas: " + n);
                return n > 0;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error en delete: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private MetodoPago map(ResultSet rs) throws SQLException {
        MetodoPago m = new MetodoPago();
        m.setIdMetodoPago(rs.getLong("ID_Metodo_Pago"));
        m.setIdUsuario(rs.getLong("ID_Usuario"));
        m.setTipo(rs.getString("Tipo"));
        m.setDetalles(rs.getString("Detalles"));
        return m;
    }
}