package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.ItemPedidoDTO;
import com.deliverytech.delivery.dto.PedidoDTO;
import com.deliverytech.delivery.entity.Pedido;

import java.math.BigDecimal;
import java.util.List;

public interface PedidoService {

    Pedido criarPedido(PedidoDTO dto);
    Pedido buscarPedidoPorId(Long id);
    List<Pedido> buscarPedidosPorCliente(Long clienteId);
    Pedido atualizarStatusPedido(Long id, String status);
    BigDecimal calcularTotalPedido(List<ItemPedidoDTO> itens);
    Pedido cancelarPedido(Long id);

}