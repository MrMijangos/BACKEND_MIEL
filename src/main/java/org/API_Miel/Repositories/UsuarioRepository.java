package org.API_Miel.Repositories;

import org.API_Miel.ConfigDB.ConfigDB;
import org.API_Miel.Models.Usuario;

import javax.sql.DataSource;
import java.sql.*;

public class UsuarioRepository {
    private final DataSource ds = ConfigDB.getDataSource();

    public Usuario findByEmail(String email) {
        String sql = "SELECT ID_Usuario, Nombre_Completo, Correo, Contrasena, Num_Celular, ID_Rol FROM Usuario WHERE Correo=?";
        
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, email);
            
            System.out.println("SQL Query: " + sql);
            System.out.println("   Buscando email: " + email);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Usuario u = map(rs);
                System.out.println("Usuario encontrado: " + u.getNombreCompleto());
                return u;
            }
            
            System.out.println("No se encontró usuario con email: " + email);
            return null;
            
        } catch (SQLException e) {
            System.err.println("ERROR en findByEmail:");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Usuario findById(long id) {
        String sql = "SELECT ID_Usuario, Nombre_Completo, Correo, Contrasena, Num_Celular, ID_Rol FROM Usuario WHERE ID_Usuario=?";
        
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) return map(rs);
            return null;
            
        } catch (SQLException e) {
            System.err.println("ERROR en findById:");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public long create(Usuario u) {
    
        String sql = "INSERT INTO Usuario(Nombre_Completo, Correo, Contrasena, Num_Celular, ID_Rol) VALUES(?,?,?,?,?)";
        
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            System.out.println("SQL Insert: " + sql);
            System.out.println("   Valores:");
            System.out.println("   1. Nombre: " + u.getNombreCompleto());
            System.out.println("   2. Correo: " + u.getCorreo());
            System.out.println("   3. Contrasena: ****");
            System.out.println("   4. Celular: " + u.getNumCelular());
            System.out.println("   5. Rol: " + u.getIdRol());
            
            ps.setString(1, u.getNombreCompleto());
            ps.setString(2, u.getCorreo());
            ps.setString(3, u.getContrasenia());
            ps.setString(4, u.getNumCelular());
            ps.setInt(5, u.getIdRol());
            
            int affectedRows = ps.executeUpdate();
            System.out.println("Filas afectadas: " + affectedRows);
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                long id = rs.getLong(1);
                System.out.println("ID generado: " + id);
                return id;
            }
            
            System.out.println("No se generó ID");
            return 0;
            
        } catch (SQLException e) {
            System.err.println("ERROR en create:");
            System.err.println("   SQLState: " + e.getSQLState());
            System.err.println("   ErrorCode: " + e.getErrorCode());
            System.err.println("   Message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Usuario map(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getLong("ID_Usuario"));
        u.setNombreCompleto(rs.getString("Nombre_Completo"));
        u.setCorreo(rs.getString("Correo"));
        u.setContrasenia(rs.getString("Contrasena")); 
        u.setNumCelular(rs.getString("Num_Celular"));
        u.setIdRol(rs.getInt("ID_Rol"));
        return u;
    }
}