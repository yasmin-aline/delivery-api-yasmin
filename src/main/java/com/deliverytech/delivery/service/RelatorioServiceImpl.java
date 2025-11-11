package com.deliverytech.delivery.service;

import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.projection.RelatorioVendasRestaurante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RelatorioServiceImpl implements RelatorioService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public List<RelatorioVendasRestaurante> relatorioVendasPorRestaurante() {
        return pedidoRepository.findTotalVendasPorRestaurante();
    }

    @Override
    public List<Pedido> relatorioPedidosPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return pedidoRepository.findByDataPedidoBetween(inicio, fim);
    }
}