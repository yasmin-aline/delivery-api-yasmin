package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.ItemPedidoDTO;
import com.deliverytech.delivery.dto.PedidoDTO;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.exception.BusinessException;
import com.deliverytech.delivery.exception.EntityNotFoundException;
import com.deliverytech.delivery.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private ProdutoService produtoService;

    @Override
    @Transactional
    public Pedido criarPedido(PedidoDTO dto) {
        Cliente cliente = clienteService.buscarClientePorId(dto.getClienteId());
        if (!cliente.getAtivo()) {
            throw new BusinessException("Cliente " + cliente.getNome() + " não está ativo.");
        }

        Restaurante restaurante = restauranteService.buscarRestaurantePorId(dto.getRestauranteId());
        if (!restaurante.getAtivo()) {
            throw new BusinessException("Restaurante " + restaurante.getNome() + " não está ativo.");
        }

        BigDecimal valorTotal = BigDecimal.ZERO;
        StringBuilder itensDescricao = new StringBuilder();

        for (ItemPedidoDTO itemDto : dto.getItens()) {
            Produto produto = produtoService.buscarProdutoPorId(itemDto.getProdutoId());
            if (!produto.getDisponivel()) {
                throw new BusinessException("Produto " + produto.getNome() + " não está disponível.");
            }
            if (!produto.getRestaurante().getId().equals(restaurante.getId())) {
                throw new BusinessException("Produto " + produto.getNome() + " não pertence ao restaurante " + restaurante.getNome());
            }

            BigDecimal subtotalItem = produto.getPreco().multiply(BigDecimal.valueOf(itemDto.getQuantidade()));
            valorTotal = valorTotal.add(subtotalItem);
            itensDescricao.append(String.format("%dx %s; ", itemDto.getQuantidade(), produto.getNome()));
        }

        valorTotal = valorTotal.add(restaurante.getTaxaEntrega());

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setValorTotal(valorTotal);
        pedido.setItens(itensDescricao.toString());
        pedido.setObservacoes(dto.getObservacoes());
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus("PENDENTE");
        pedido.setNumeroPedido("PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public Pedido buscarPedidoPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> buscarPedidosPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    @Override
    @Transactional
    public Pedido atualizarStatusPedido(Long id, String status) {
        Pedido pedido = buscarPedidoPorId(id);
        if (pedido.getStatus().equals("ENTREGUE") || pedido.getStatus().equals("CANCELADO")) {
            throw new BusinessException("Não é possível alterar o status de um pedido que já foi " + pedido.getStatus());
        }
        pedido.setStatus(status.toUpperCase());
        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public Pedido cancelarPedido(Long id) {
        Pedido pedido = buscarPedidoPorId(id);
        if (pedido.getStatus().equals("PENDENTE") || pedido.getStatus().equals("CONFIRMADO")) {
            pedido.setStatus("CANCELADO");
            return pedidoRepository.save(pedido);
        } else {
            throw new BusinessException("Não é possível cancelar um pedido com status " + pedido.getStatus());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalPedido(List<ItemPedidoDTO> itens) {
        BigDecimal valorTotal = BigDecimal.ZERO;
        for (ItemPedidoDTO itemDto : itens) {
            Produto produto = produtoService.buscarProdutoPorId(itemDto.getProdutoId());
            BigDecimal subtotalItem = produto.getPreco().multiply(BigDecimal.valueOf(itemDto.getQuantidade()));
            valorTotal = valorTotal.add(subtotalItem);
        }
        return valorTotal;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> buscarPedidosPorRestaurante(Long restauranteId) {
        return pedidoRepository.findByRestauranteId(restauranteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> buscarPedidosFiltrados(String status, LocalDateTime inicio, LocalDateTime fim) {
        if (status != null && inicio != null && fim != null) {
            return pedidoRepository.findByStatusAndDataPedidoBetween(status, inicio, fim);
        }
        if (status != null) {
            return pedidoRepository.findByStatus(status);
        }
        if (inicio != null && fim != null) {
            return pedidoRepository.findByDataPedidoBetween(inicio, fim);
        }
        return pedidoRepository.findAll();
    }
}