package org.API_Miel.Services;

import org.API_Miel.Models.Producto;
import org.API_Miel.Repositories.ProductoRepository;

import java.util.List;
import java.util.Map;

public class ProductService {
    private final ProductoRepository repo = new ProductoRepository();

    public List<Producto> list() {
        return repo.findAll();
    }

    public Producto get(long id) {
        return repo.findById(id);
    }

    public long create(Producto p) {
        return repo.create(p);
    }

    public void update(long id, Producto p) {
        repo.update(id, p);
    }

    public void delete(long id) {
        repo.delete(id);
    }

    public List<Map<String, Object>> listStockChart() {
        return repo.findNamesAndStock();
    }
    // LO DE LA GRAFICA DE STOCK DE PRODUCTOS

    public List<Map<String, Object>> getTopSelling() {
        return repo.findTopSelling();
    }
}
