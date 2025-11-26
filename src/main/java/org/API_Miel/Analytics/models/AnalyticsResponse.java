package org.API_Miel.Analytics.models;

import java.math.BigDecimal;
import java.util.List;

public class AnalyticsResponse {
    private int totalProductosDiferentes;
    private int totalUnidadesVendidas;
    private BigDecimal gananciasTotales;
    private List<ProductSalesDTO> detallePorProducto;

    // Getters y Setters
    public int getTotalProductosDiferentes() { return totalProductosDiferentes; }
    public void setTotalProductosDiferentes(int totalProductosDiferentes) { this.totalProductosDiferentes = totalProductosDiferentes; }
    
    public int getTotalUnidadesVendidas() { return totalUnidadesVendidas; }
    public void setTotalUnidadesVendidas(int totalUnidadesVendidas) { this.totalUnidadesVendidas = totalUnidadesVendidas; }
    
    public BigDecimal getGananciasTotales() { return gananciasTotales; }
    public void setGananciasTotales(BigDecimal gananciasTotales) { this.gananciasTotales = gananciasTotales; }
    
    public List<ProductSalesDTO> getDetallePorProducto() { return detallePorProducto; }
    public void setDetallePorProducto(List<ProductSalesDTO> detallePorProducto) { this.detallePorProducto = detallePorProducto; }
}