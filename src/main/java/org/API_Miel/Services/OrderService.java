package org.API_Miel.Services;

import org.API_Miel.Models.Pedido;
import org.API_Miel.Repositories.PedidoRepository;

import java.util.List;
import java.util.Map;

public class OrderService {
    private final PedidoRepository repo = new PedidoRepository();

    public long crear(long idUsuario, long idMetodoPago, long idDireccion) { 
        return repo.crearPedido(idUsuario, idMetodoPago, idDireccion); 
    }
    
    public long crearCompleto(Pedido pedido) { 
        return repo.crearPedidoCompleto(pedido); 
    }
    public void updateOrderStatus(long orderId, String newStatus) {
        repo.updateStatus(orderId, newStatus);
    }
    
    public List<Pedido> listar(long idUsuario) { 
        return repo.listarPorUsuario(idUsuario); 
    }
    
    public List<Map<String, Object>> listarTodos() { 
        return repo.listarParaAdmin(); 
    }
}