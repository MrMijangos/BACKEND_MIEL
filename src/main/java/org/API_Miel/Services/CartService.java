package org.API_Miel.Services;

import java.util.List;
import java.util.Map;

import org.API_Miel.Repositories.CartRepository;

public class CartService {
    private final CartRepository repo = new CartRepository();

    public List<Map<String, Object>> getCart(long idUsuario) {
        System.out.println("🔍 CartService.getCart - Usuario ID: " + idUsuario);
        return repo.getCart(idUsuario);
    }

    public void addToCart(long idUsuario, long idProducto, int cantidad) {
        repo.addToCart(idUsuario, idProducto, cantidad);
    }

    public void removeItem(long idDetalle) {
        repo.deleteItem(idDetalle);
    }

    public void clearCart(long idUsuario) {
        repo.clearCart(idUsuario);
    }
}