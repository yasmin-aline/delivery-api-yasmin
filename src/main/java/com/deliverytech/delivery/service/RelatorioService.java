package com.deliverytech.delivery.service;

import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.repository.projection.RelatorioVendasRestaurante;

import java.time.LocalDateTime;
import java.util.List;

public interface RelatorioService {

    List<RelatorioVendasRestaurante> relatorioVendasPorRestaurante();

    List<Pedido> relatorioPedidosPorPeriodo(LocalDateTime inicio, LocalDateTime fim);
}