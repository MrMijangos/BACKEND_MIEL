package org.API_Miel.Analytics.models;

import java.math.BigDecimal;

public class ProductSalesDTO {
    private String productoNombre;
    private int cantidadVendida;
    private BigDecimal gananciaTotal;

    public ProductSalesDTO(String productoNombre, int cantidadVendida, BigDecimal gananciaTotal) {
        this.productoNombre = productoNombre;
        this.cantidadVendida = cantidadVendida;
        this.gananciaTotal = gananciaTotal;
    }

    // Getters
    public String getProductoNombre() { return productoNombre; }
    public int getCantidadVendida() { return cantidadVendida; }
    public BigDecimal getGananciaTotal() { return gananciaTotal; }
}