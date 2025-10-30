package com.deliverytech.delivery.service;

import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private RestauranteService restauranteService;

    /**
     * Criar novo pedido
     */
    public Pedido criar(Pedido pedido, Long clienteId, Long restauranteId) {
        Cliente cliente = clienteService.buscarPorId(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + clienteId)); // [cite: 271]

        Restaurante restaurante = restauranteService.buscarPorId(restauranteId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + restauranteId)); // [cite: 271]

        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus("PENDENTE");
        pedido.setNumeroPedido("PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        if (pedido.getValorTotal() == null || pedido.getValorTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor total do pedido deve ser maior que zero");
        }

        return pedidoRepository.save(pedido);
    }

    /**
     * Buscar pedido por ID
     */
    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    /**
     * Listar pedidos de um cliente
     */
    @Transactional(readOnly = true)
    public List<Pedido> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    /**
     * Atualizar status do pedido
     */
    public Pedido atualizarStatus(Long id, String novoStatus) {
        Pedido pedido = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + id));

        if (pedido.getStatus().equals("ENTREGUE") && novoStatus.equals("CANCELADO")) {
            throw new IllegalArgumentException("Não é possível cancelar um pedido já entregue");
        }

        pedido.setStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }
}
