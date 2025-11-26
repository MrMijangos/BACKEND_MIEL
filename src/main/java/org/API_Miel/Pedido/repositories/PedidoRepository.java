package org.API_Miel.Pedido.repositories;

import org.API_Miel.ConfigDB.DatabaseConfig;
import org.API_Miel.Pedido.models.Pedido;
import org.API_Miel.Pedido.models.PedidoDetalle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PedidoRepository {

    public Pedido save(Pedido pedido) {
        String sql = "INSERT INTO pedido (numero_pedido, usuario_id, metodo_pago_id, direccion_id, " +
                     "subtotal, costo_envio, total, estado, notas_cliente) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, pedido.getNumeroPedido());
            stmt.setLong(2, pedido.getUsuarioId());
            stmt.setLong(3, pedido.getMetodoPagoId());
            stmt.setLong(4, pedido.getDireccionId());
            stmt.setBigDecimal(5, pedido.getSubtotal());
            stmt.setBigDecimal(6, pedido.getCostoEnvio());
            stmt.setBigDecimal(7, pedido.getTotal());
            stmt.setString(8, pedido.getEstado());
            stmt.setString(9, pedido.getNotasCliente());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Error al crear el pedido, no se insertaron filas.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pedido.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Error al crear el pedido, no se obtuvo el ID.");
                }
            }

            return pedido;

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar pedido: " + e.getMessage(), e);
        }
    }

    public void update(Pedido pedido) {
        String sql = "UPDATE pedido SET subtotal = ?, total = ?, estado = ?, fecha_entrega = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, pedido.getSubtotal());
            stmt.setBigDecimal(2, pedido.getTotal());
            stmt.setString(3, pedido.getEstado());
            stmt.setTimestamp(4, pedido.getFechaEntrega()); // Por si se actualiza entrega
            stmt.setLong(5, pedido.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar pedido", e);
        }
    }

    public void saveDetalle(PedidoDetalle detalle) {
        String sql = "INSERT INTO pedido_detalle (pedido_id, producto_id, nombre_producto, " +
                     "cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, detalle.getPedidoId());
            stmt.setLong(2, detalle.getProductoId());
            stmt.setString(3, detalle.getNombreProducto());
            stmt.setInt(4, detalle.getCantidad());
            stmt.setBigDecimal(5, detalle.getPrecioUnitario());
            stmt.setBigDecimal(6, detalle.getSubtotal());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar detalle de pedido", e);
        }
    }

    public Optional<Pedido> findById(Long id) {
        String sql = "SELECT * FROM pedido WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPedido(rs));
                }
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar pedido", e);
        }
    }

    public List<PedidoDetalle> findDetallesByPedidoId(Long pedidoId) {
        String sql = "SELECT * FROM pedido_detalle WHERE pedido_id = ?";
        List<PedidoDetalle> detalles = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, pedidoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    detalles.add(mapResultSetToDetalle(rs));
                }
            }
            return detalles;

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar detalles de pedido", e);
        }
    }

    public List<Pedido> findByUsuarioId(Long usuarioId) {
        String sql = "SELECT * FROM pedido WHERE usuario_id = ? ORDER BY fecha_pedido DESC";
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapResultSetToPedido(rs));
                }
            }
            return pedidos;

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar pedidos del usuario", e);
        }
    }

    public List<Pedido> findAll() {
        String sql = "SELECT * FROM pedido ORDER BY fecha_pedido DESC";
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pedidos.add(mapResultSetToPedido(rs));
            }
            return pedidos;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar pedidos", e);
        }
    }

    public void updateEstado(Long id, String estado) {
        String sql = "UPDATE pedido SET estado = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estado);
            stmt.setLong(2, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estado de pedido", e);
        }
    }

    public String generateNumeroPedido() {
        return "PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private Pedido mapResultSetToPedido(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setId(rs.getLong("id"));
        pedido.setNumeroPedido(rs.getString("numero_pedido"));
        pedido.setUsuarioId(rs.getLong("usuario_id"));
        pedido.setMetodoPagoId(rs.getLong("metodo_pago_id"));
        pedido.setDireccionId(rs.getLong("direccion_id"));
        pedido.setSubtotal(rs.getBigDecimal("subtotal"));
        pedido.setCostoEnvio(rs.getBigDecimal("costo_envio"));
        pedido.setTotal(rs.getBigDecimal("total"));
        pedido.setEstado(rs.getString("estado"));
        pedido.setNotasCliente(rs.getString("notas_cliente"));
        pedido.setFechaPedido(rs.getTimestamp("fecha_pedido"));
        pedido.setFechaActualizacion(rs.getTimestamp("fecha_actualizacion"));
        pedido.setFechaEntrega(rs.getTimestamp("fecha_entrega"));
        return pedido;
    }

    private PedidoDetalle mapResultSetToDetalle(ResultSet rs) throws SQLException {
        PedidoDetalle detalle = new PedidoDetalle();
        detalle.setId(rs.getLong("id"));
        detalle.setPedidoId(rs.getLong("pedido_id"));
        detalle.setProductoId(rs.getLong("producto_id"));
        detalle.setNombreProducto(rs.getString("nombre_producto"));
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
        detalle.setSubtotal(rs.getBigDecimal("subtotal"));
        return detalle;
    }
}