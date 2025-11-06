package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.ItemPedidoDTO;
import com.deliverytech.delivery.dto.PedidoDTO;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.service.PedidoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/api/pedidos")
    public ResponseEntity<?> criarPedido(@Valid @RequestBody PedidoDTO dto) {
        try {
            Pedido pedidoSalvo = pedidoService.criarPedido(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("/api/pedidos/{id}")
    public ResponseEntity<?> buscarPedidoCompleto(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.buscarPedidoPorId(id);
            return ResponseEntity.ok(pedido);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/clientes/{clienteld}/pedidos")
    public ResponseEntity<List<Pedido>> buscarPedidosDoCliente(@PathVariable Long clienteld) {
        return ResponseEntity.ok(pedidoService.buscarPedidosPorCliente(clienteld));
    }

    @PatchMapping("/api/pedidos/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Pedido pedido = pedidoService.atualizarStatusPedido(id, status);
            return ResponseEntity.ok(pedido);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @DeleteMapping("/api/pedidos/{id}")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.cancelarPedido(id);
            return ResponseEntity.ok(pedido);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @PostMapping("/api/pedidos/calcular")
    public ResponseEntity<?> calcularTotal(@Valid @RequestBody List<ItemPedidoDTO> itens) {
        try {
            BigDecimal total = pedidoService.calcularTotalPedido(itens);
            return ResponseEntity.ok().body(java.util.Map.of("totalCalculado", total));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
}