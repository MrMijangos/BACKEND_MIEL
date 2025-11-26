package org.API_Miel.Analytics.repositories;

import org.API_Miel.Analytics.models.ProductSalesDTO;
import org.API_Miel.ConfigDB.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnalyticsRepository {

    public List<ProductSalesDTO> getSalesByProduct() {
        // Query: Suma cantidad y subtotal agrupado por producto
        // Filtro: Solo pedidos que NO estén cancelados ni pendientes
        String sql = "SELECT " +
                     "   p.nombre AS nombre_producto, " +
                     "   SUM(pd.cantidad) AS total_cantidad, " +
                     "   SUM(pd.subtotal) AS total_ganancia " +
                     "FROM pedido_detalle pd " +
                     "JOIN pedido ped ON pd.pedido_id = ped.id " +
                     "JOIN producto p ON pd.producto_id = p.id " +
                     "WHERE ped.estado NOT IN ('cancelado', 'pendiente') " +
                     "GROUP BY p.id, p.nombre " +
                     "ORDER BY total_ganancia DESC";

        List<ProductSalesDTO> sales = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                sales.add(new ProductSalesDTO(
                    rs.getString("nombre_producto"),
                    rs.getInt("total_cantidad"),
                    rs.getBigDecimal("total_ganancia")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener analytics", e);
        }
        return sales;
    }
}