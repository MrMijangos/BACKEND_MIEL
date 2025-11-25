package org.API_Miel.Repositories;

import org.API_Miel.ConfigDB.ConfigDB;
import javax.sql.DataSource;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartRepository {
    private final DataSource ds = ConfigDB.getDataSource();

   
    public List<Map<String, Object>> getCart(long idUsuario) {
        List<Map<String, Object>> items = new ArrayList<>();
        
        String sql = "SELECT cd.ID_Detalle, p.ID_Producto, p.Nombre, p.Precio, p.Imagen, cd.Cantidad " +
                     "FROM Carrito_Detalle cd " +
                     "JOIN Producto p ON cd.ID_Producto = p.ID_Producto " +
                     "JOIN Carrito c ON cd.ID_Carrito = c.ID_Carrito " +
                     "WHERE c.ID_Usuario = ?";

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                // Nombres de claves coinciden con lo que espera el Frontend
                item.put("ID_Detalle", rs.getLong("ID_Detalle")); 
                item.put("id_producto", rs.getLong("ID_Producto"));
                item.put("nombre", rs.getString("Nombre"));
                item.put("precio", rs.getBigDecimal("Precio"));
                item.put("imagen", rs.getString("Imagen"));
                item.put("cantidad", rs.getInt("Cantidad"));
                
                items.add(item);
            }
            return items;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener el carrito: " + e.getMessage());
        }
    }

    public void addToCart(long idUsuario, long idProducto, int cantidad) {
        String sqlCheckCart = "SELECT ID_Carrito FROM Carrito WHERE ID_Usuario = ?";
        String sqlCreateCart = "INSERT INTO Carrito (ID_Usuario) VALUES (?)";
        String sqlCheckItem = "SELECT ID_Detalle FROM Carrito_Detalle WHERE ID_Carrito = ? AND ID_Producto = ?";
        String sqlUpdateItem = "UPDATE Carrito_Detalle SET Cantidad = Cantidad + ? WHERE ID_Detalle = ?";
        String sqlInsertItem = "INSERT INTO Carrito_Detalle (ID_Carrito, ID_Producto, Cantidad) VALUES (?, ?, ?)";

        try (Connection c = ds.getConnection()) {
            c.setAutoCommit(false); 

            try {
             
                long idCarrito = -1;
                try (PreparedStatement psCheck = c.prepareStatement(sqlCheckCart)) {
                    psCheck.setLong(1, idUsuario);
                    ResultSet rs = psCheck.executeQuery();
                    if (rs.next()) {
                        idCarrito = rs.getLong("ID_Carrito");
                    }
                }

                if (idCarrito == -1) {
                    try (PreparedStatement psCreate = c.prepareStatement(sqlCreateCart, Statement.RETURN_GENERATED_KEYS)) {
                        psCreate.setLong(1, idUsuario);
                        psCreate.executeUpdate();
                        ResultSet rsKeys = psCreate.getGeneratedKeys();
                        if (rsKeys.next()) {
                            idCarrito = rsKeys.getLong(1);
                        } else {
                            throw new SQLException("No se pudo crear el carrito.");
                        }
                    }
                }

                long idDetalle = -1;
                try (PreparedStatement psItem = c.prepareStatement(sqlCheckItem)) {
                    psItem.setLong(1, idCarrito);
                    psItem.setLong(2, idProducto);
                    ResultSet rsItem = psItem.executeQuery();
                    if (rsItem.next()) {
                        idDetalle = rsItem.getLong("ID_Detalle");
                    }
                }

                if (idDetalle != -1) {
                    try (PreparedStatement psUpdate = c.prepareStatement(sqlUpdateItem)) {
                        psUpdate.setInt(1, cantidad);
                        psUpdate.setLong(2, idDetalle);
                        psUpdate.executeUpdate();
                    }
                } else {
                    try (PreparedStatement psInsert = c.prepareStatement(sqlInsertItem)) {
                        psInsert.setLong(1, idCarrito);
                        psInsert.setLong(2, idProducto);
                        psInsert.setInt(3, cantidad);
                        psInsert.executeUpdate();
                    }
                }

                c.commit();

            } catch (SQLException e) {
                c.rollback(); 
                throw e;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al agregar al carrito: " + e.getMessage());
        }
    }

  
    public void deleteItem(long idDetalle) {
        String sql = "DELETE FROM Carrito_Detalle WHERE ID_Detalle = ?";
        
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setLong(1, idDetalle);
            ps.executeUpdate();
            
        } catch (SQLException e) { 
            throw new RuntimeException("Error al eliminar item: " + e.getMessage()); 
        }
    }

  
    public void clearCart(long idUsuario) {
        String sql = "DELETE FROM Carrito_Detalle WHERE ID_Carrito = (SELECT ID_Carrito FROM Carrito WHERE ID_Usuario = ?)";
        
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setLong(1, idUsuario);
            ps.executeUpdate();
            
        } catch (SQLException e) { 
            throw new RuntimeException("Error al vaciar carrito: " + e.getMessage()); 
        }
    }
}