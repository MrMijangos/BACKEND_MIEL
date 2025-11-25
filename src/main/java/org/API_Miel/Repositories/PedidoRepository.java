package org.API_Miel.Repositories;

import org.API_Miel.ConfigDB.ConfigDB;
import org.API_Miel.Models.Pedido;

import javax.sql.DataSource;
import java.sql.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PedidoRepository {
    private final DataSource ds = ConfigDB.getDataSource();

   
    public long crearPedido(long idUsuario, long idMetodoPago, long idDireccion) {
        try (Connection c = ds.getConnection()) {
            c.setAutoCommit(false);
            BigDecimal total = BigDecimal.ZERO;
            
            try (PreparedStatement sum = c.prepareStatement("SELECT cd.Cantidad*p.Precio FROM Carrito_Detalle cd JOIN Producto p ON cd.ID_Producto=p.ID_Producto WHERE cd.ID_Carrito=(SELECT ID_Carrito FROM Carrito WHERE ID_Usuario=?)")) {
                sum.setLong(1, idUsuario);
                ResultSet rs = sum.executeQuery();
                while (rs.next()) total = total.add(rs.getBigDecimal(1));
            }
            
            String numero = "PED" + System.currentTimeMillis();
            long idPedido;
            try (PreparedStatement ins = c.prepareStatement("INSERT INTO Pedido(Numero_Pedido,ID_Usuario,ID_Metodo_Pago,ID_Direccion,Total,Estado,Fecha) VALUES(?,?,?,?,?,'CREADO',?)", Statement.RETURN_GENERATED_KEYS)) {
                ins.setString(1, numero);
                ins.setLong(2, idUsuario);
                ins.setLong(3, idMetodoPago);
                ins.setLong(4, idDireccion);
                ins.setBigDecimal(5, total);
                ins.setTimestamp(6, Timestamp.from(Instant.now())); 
                ins.executeUpdate();
                ResultSet rs = ins.getGeneratedKeys();
                rs.next();
                idPedido = rs.getLong(1);
            }
            
            try (PreparedStatement items = c.prepareStatement("SELECT cd.ID_Producto, cd.Cantidad, p.Precio FROM Carrito_Detalle cd JOIN Producto p ON cd.ID_Producto=p.ID_Producto WHERE cd.ID_Carrito=(SELECT ID_Carrito FROM Carrito WHERE ID_Usuario=?)")) {
                items.setLong(1, idUsuario);
                ResultSet rs = items.executeQuery();
                while (rs.next()) {
                    long idProd = rs.getLong(1);
                    int cant = rs.getInt(2);
                    BigDecimal subtotal = rs.getBigDecimal(3).multiply(BigDecimal.valueOf(cant));
                    try (PreparedStatement det = c.prepareStatement("INSERT INTO Pedido_Detalle(ID_Pedido,ID_Producto,Cantidad,Subtotal) VALUES(?,?,?,?)")) {
                        det.setLong(1, idPedido);
                        det.setLong(2, idProd);
                        det.setInt(3, cant);
                        det.setBigDecimal(4, subtotal);
                        det.executeUpdate();
                    }
                }
            }
            
            try (PreparedStatement clear = c.prepareStatement("DELETE FROM Carrito_Detalle WHERE ID_Carrito=(SELECT ID_Carrito FROM Carrito WHERE ID_Usuario=?)")) {
                clear.setLong(1, idUsuario);
                clear.executeUpdate();
            }
            c.commit();
            return idPedido;
        } catch (SQLException e) { 
            throw new RuntimeException("Error creando pedido: " + e.getMessage(), e); 
        }
    }

public void updateStatus(long id, String estado) {
        String sql = "UPDATE Pedido SET Estado = ? WHERE ID_Pedido = ?"; 
        
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, estado.toUpperCase()); 
            ps.setLong(2, id);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                 System.err.println("Advertencia: No se encontró el pedido con ID " + id + " para actualizar.");
               
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error en updateStatus: " + e.getMessage());
            throw new RuntimeException("Error DB al actualizar estado del pedido.", e);
        }
    }
 
    public long crearPedidoCompleto(Pedido pedido) {
        try (Connection c = ds.getConnection()) {
            c.setAutoCommit(false);
            
            if (!verificarDireccionExiste(pedido.getIdDireccion())) {
                throw new RuntimeException("La dirección con ID " + pedido.getIdDireccion() + " no existe en la base de datos");
            }
            
            String numero = "PED" + System.currentTimeMillis();
            long idPedido;
            try (PreparedStatement ins = c.prepareStatement(
                "INSERT INTO Pedido(Numero_Pedido, ID_Usuario, ID_Metodo_Pago, ID_Direccion, Total, Estado, Fecha) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                
                ins.setString(1, numero);
                ins.setLong(2, pedido.getIdUsuario());
                ins.setLong(3, pedido.getIdMetodoPago());
                ins.setLong(4, pedido.getIdDireccion());
                ins.setBigDecimal(5, pedido.getTotal());
                ins.setString(6, pedido.getEstado() != null ? pedido.getEstado() : "COMPLETADA");
                ins.setTimestamp(7, Timestamp.from(Instant.now())); 
                ins.executeUpdate();
                ResultSet rs = ins.getGeneratedKeys();
                rs.next();
                idPedido = rs.getLong(1);
            }
            
            if (pedido.getDetalles() != null && !pedido.getDetalles().isEmpty()) {
                for (Map<String, Object> detalle : pedido.getDetalles()) {
                    try (PreparedStatement det = c.prepareStatement(
                        "INSERT INTO Pedido_Detalle(ID_Pedido, ID_Producto, Cantidad, Subtotal, Precio_Unitario) " +
                        "VALUES(?, ?, ?, ?, ?)")) {
                        
                        Long idProducto = getLongFromObject(detalle.get("id_producto"));
                        Integer cantidad = getIntegerFromObject(detalle.get("cantidad"));
                        BigDecimal precio = getBigDecimalFromObject(detalle.get("precio"));
                        
                        if (idProducto == null || cantidad == null || precio == null) {
                            throw new RuntimeException("Datos de detalle inválidos: " + detalle);
                        }
                        
                        BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(cantidad));
                        
                        det.setLong(1, idPedido);
                        det.setLong(2, idProducto);
                        det.setInt(3, cantidad);
                        det.setBigDecimal(4, subtotal);
                        det.setBigDecimal(5, precio);
                        det.executeUpdate();
                    }
                }
            }
            
            try (PreparedStatement clear = c.prepareStatement(
                "DELETE FROM Carrito_Detalle WHERE ID_Carrito=(SELECT ID_Carrito FROM Carrito WHERE ID_Usuario=?)")) {
                clear.setLong(1, pedido.getIdUsuario());
                clear.executeUpdate();
            }
            
            c.commit();
            return idPedido;
        } catch (SQLException e) { 
            throw new RuntimeException("Error creando pedido completo: " + e.getMessage(), e); 
        }
    }

    private boolean verificarDireccionExiste(long idDireccion) {
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT 1 FROM Direccion WHERE ID_Direccion = ?")) {
            ps.setLong(1, idDireccion);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Error verificando dirección: " + e.getMessage(), e);
        }
    }

    private Long getLongFromObject(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Long) return (Long) obj;
        if (obj instanceof Integer) return ((Integer) obj).longValue();
        try { return Long.parseLong(obj.toString()); } 
        catch (NumberFormatException e) { return null; }
    }
    
    private Integer getIntegerFromObject(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Integer) return (Integer) obj;
        if (obj instanceof Long) return ((Long) obj).intValue();
        try { return Integer.parseInt(obj.toString()); } 
        catch (NumberFormatException e) { return null; }
    }
    
    private BigDecimal getBigDecimalFromObject(Object obj) {
        if (obj == null) return null;
        if (obj instanceof BigDecimal) return (BigDecimal) obj;
        if (obj instanceof Double) return BigDecimal.valueOf((Double) obj);
        if (obj instanceof Integer) return BigDecimal.valueOf((Integer) obj);
        try { return new BigDecimal(obj.toString()); } 
        catch (NumberFormatException e) { return null; }
    }

    public List<Pedido> listarPorUsuario(long idUsuario) {
        List<Pedido> list = new ArrayList<>();
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT ID_Pedido, Numero_Pedido, ID_Usuario, ID_Metodo_Pago, ID_Direccion, Total, Estado, Fecha FROM Pedido WHERE ID_Usuario=? ORDER BY Fecha DESC")) {
            ps.setLong(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private Pedido map(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setIdPedido(rs.getInt(1));
        p.setNumeroPedido(rs.getString(2));
        p.setIdUsuario(rs.getInt(3));
        p.setIdMetodoPago(rs.getLong(4));
        p.setIdDireccion(rs.getLong(5));
        p.setTotal(rs.getBigDecimal(6));
        p.setEstado(rs.getString(7));
        
        Timestamp ts = rs.getTimestamp(8);
        if(ts != null) p.setFecha(ts.toInstant());
        
        return p;
    }
    
    public List<Map<String, Object>> listarParaAdmin() {
        List<Map<String, Object>> list = new ArrayList<>();
        
        String sql = "SELECT p.ID_Pedido, p.Numero_Pedido, p.Total, p.Estado, p.Fecha, " +
                     "u.Nombre_Completo, u.Num_Celular, " + 
                     "d.Calle, d.Colonia, d.Ciudad, d.Estado as EstadoDir, d.Codigo_Postal " +
                     "FROM Pedido p " +
                     "JOIN Usuario u ON p.ID_Usuario = u.ID_Usuario " +
                     "JOIN Direccion d ON p.ID_Direccion = d.ID_Direccion " +  
                     "ORDER BY p.Fecha DESC";

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("idPedido", rs.getLong("ID_Pedido"));
                map.put("numeroPedido", rs.getString("Numero_Pedido"));
                map.put("total", rs.getBigDecimal("Total"));
                map.put("estado", rs.getString("Estado"));
                
                Timestamp ts = rs.getTimestamp("Fecha");
                map.put("fecha", (ts != null) ? ts.toInstant().toString() : null);
                
                map.put("nombreUsuario", rs.getString("Nombre_Completo"));
                
                String celular = rs.getString("Num_Celular");
                map.put("telefono", (celular != null) ? celular : "N/A");
                
                // Construir dirección
                String calle = rs.getString("Calle");
                String col = rs.getString("Colonia");
                String ciudad = rs.getString("Ciudad");
                String est = rs.getString("EstadoDir");
                String cp = rs.getString("Codigo_Postal");

                String dir = (calle != null ? calle : "") + "<br>" +
                             (col != null ? "Col. " + col : "") + "<br>" +
                             (ciudad != null ? ciudad : "") + ", " + 
                             (est != null ? est : "") + "<br>" +
                             "CP: " + (cp != null ? cp : "");
                
                map.put("direccionCompleta", dir);

                list.add(map);
            }
            return list;
        } catch (SQLException e) { 
            System.err.println("ERROR SQL en listarParaAdmin: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e); 
        }
    }
}