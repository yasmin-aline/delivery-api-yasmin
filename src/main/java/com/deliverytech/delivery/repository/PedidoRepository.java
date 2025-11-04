package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.repository.projection.RelatorioVendasRestaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Buscar pedidos por cliente
    List<Pedido> findByClienteId(Long clienteId);

    // Buscar pedidos por restaurante
    List<Pedido> findByRestauranteId(Long restauranteId);

    // Filtrar por status
    List<Pedido> findByStatus(String status);

    // Filtrar por data
    List<Pedido> findByDataPedidoBetween(LocalDateTime dataInicio, LocalDateTime dataFim);

    // Buscar os 10 pedidos mais recentes, order by data
    List<Pedido> findTop10ByOrderByDataPedidoDesc();

    @Query("SELECT p FROM Pedido p WHERE p.valorTotal > :valor")
    List<Pedido> findPedidosComValorAcimaDe(BigDecimal valor);


    @Query("SELECT p FROM Pedido p WHERE p.dataPedido BETWEEN :inicio AND :fim AND p.status = :status")
    List<Pedido> findRelatorioPorPeriodoEStatus(LocalDateTime inicio, LocalDateTime fim, String status);


    @Query("SELECT p.restaurante.nome as nomeRestaurante, SUM(p.valorTotal) as totalVendas " +
            "FROM Pedido p " +
            "GROUP BY p.restaurante.nome")
    List<RelatorioVendasRestaurante> findTotalVendasPorRestaurante();
}
