package org.API_Miel.Repositories;

import org.API_Miel.ConfigDB.ConfigDB;
import org.API_Miel.Models.Producto;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoRepository {
    private final DataSource ds = ConfigDB.getDataSource();

    public List<Producto> findAll() {
        List<Producto> list = new ArrayList<>();
        try (Connection c = ds.getConnection();
                PreparedStatement ps = c.prepareStatement(
                        "SELECT ID_Producto, Nombre, Descripcion, Precio, Stock, Imagen FROM producto")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(map(rs));
        } catch (SQLException e) {
            System.err.println("Error en findAll: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return list;
    }

    public Producto findById(long id) {
        try (Connection c = ds.getConnection();
                PreparedStatement ps = c.prepareStatement(
                        "SELECT ID_Producto, Nombre, Descripcion, Precio, Stock, Imagen FROM producto WHERE ID_Producto=?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return map(rs);
            return null;
        } catch (SQLException e) {
            System.err.println(" Error en findById: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public long create(Producto p) {
        try (Connection c = ds.getConnection();
                PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO producto(Nombre,Descripcion,Precio,Stock,Imagen) VALUES(?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStock());
            ps.setString(5, p.getImagen());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next())
                return rs.getLong(1);
            return 0;
        } catch (SQLException e) {
            System.err.println(" Error en create: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void update(long id, Producto p) {
        try (Connection c = ds.getConnection();
                PreparedStatement ps = c.prepareStatement(
                        "UPDATE producto SET Nombre=?,Descripcion=?,Precio=?,Stock=?,Imagen=? WHERE ID_Producto=?")) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStock());
            ps.setString(5, p.getImagen());
            ps.setLong(6, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(" Error en update: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void delete(long id) {
        try (Connection c = ds.getConnection();
                PreparedStatement ps = c.prepareStatement("DELETE FROM producto WHERE ID_Producto=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(" Error en delete: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Map<String, Object>> findNamesAndStock() {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection c = ds.getConnection();
                PreparedStatement ps = c.prepareStatement("SELECT Nombre, Stock FROM producto")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> m = new HashMap<>();
                m.put("nombre", rs.getString("Nombre"));
                m.put("stock", rs.getInt("Stock"));
                list.add(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;// ACA ESTA LO DE ENCONTRAR NOMBRE Y STOCK DE LOS PRODUCTOS
    }

    public List<Map<String, Object>> findTopSelling() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT p.ID_Producto, p.Nombre, " +
                "SUM(pd.Cantidad) as TotalVendido, " +
                "SUM(pd.Subtotal) as Ganancias " +
                "FROM Producto p " +
                "JOIN Pedido_Detalle pd ON p.ID_Producto = pd.ID_Producto " +
                "GROUP BY p.ID_Producto, p.Nombre " +
                "ORDER BY TotalVendido DESC";

        try (Connection c = ds.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> m = new HashMap<>();
                m.put("idProducto", rs.getLong("ID_Producto"));
                m.put("nombre", rs.getString("Nombre"));
                m.put("cantidadVendida", rs.getInt("TotalVendido"));
                m.put("ganancias", rs.getDouble("Ganancias"));
                list.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Error en findTopSelling: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return list;
    }

    private Producto map(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setIdProducto(rs.getLong("ID_Producto"));
        p.setNombre(rs.getString("Nombre"));
        p.setDescripcion(rs.getString("Descripcion"));
        p.setPrecio(rs.getDouble("Precio"));
        p.setStock(rs.getInt("Stock"));
        p.setImagen(rs.getString("Imagen"));
        return p;
    }
}
