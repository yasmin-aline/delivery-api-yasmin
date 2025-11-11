package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.repository.projection.RelatorioVendasRestaurante;
import com.deliverytech.delivery.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin(origins = "*")
@Tag(name = "Relatórios", description = "Endpoints para extração de dados e relatórios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/vendas-por-restaurante")
    @Operation(summary = "Relatório de total de vendas por restaurante")
    public ResponseEntity<List<RelatorioVendasRestaurante>> getVendasPorRestaurante() {
        return ResponseEntity.ok(relatorioService.relatorioVendasPorRestaurante());
    }

    @GetMapping("/pedidos-por-periodo")
    @Operation(summary = "Relatório de pedidos por período", description = "Retorna todos os pedidos entre duas datas")
    public ResponseEntity<List<Pedido>> getPedidosPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(relatorioService.relatorioPedidosPorPeriodo(inicio, fim));
    }
}