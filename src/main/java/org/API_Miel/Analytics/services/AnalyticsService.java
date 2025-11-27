package org.API_Miel.Analytics.services;

import org.API_Miel.Analytics.models.AnalyticsResponse;
import org.API_Miel.Analytics.models.ProductSalesDTO;
import org.API_Miel.Analytics.repositories.AnalyticsRepository;

import java.math.BigDecimal;
import java.util.List;

public class AnalyticsService {
    private final AnalyticsRepository repository;

    public AnalyticsService() {
        this.repository = new AnalyticsRepository();
    }

    public AnalyticsResponse getGeneralAnalytics() {
        List<ProductSalesDTO> sales = repository.getSalesByProduct();

        AnalyticsResponse response = new AnalyticsResponse();
        response.setDetallePorProducto(sales);
        
        int totalUnidades = 0;
        BigDecimal totalGanancia = BigDecimal.ZERO;

        for (ProductSalesDTO item : sales) {
            totalUnidades += item.getCantidadVendida();
            totalGanancia = totalGanancia.add(item.getGananciaTotal());
        }

        response.setTotalProductosDiferentes(sales.size()); // Cantidad de productos únicos vendidos
        response.setTotalUnidadesVendidas(totalUnidades);
        response.setGananciasTotales(totalGanancia);

        return response;
    }
}