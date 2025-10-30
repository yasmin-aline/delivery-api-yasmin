package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    /**
     * Criar novo pedido
     */
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Pedido pedido) {
        try {
            if (pedido.getCliente() == null || pedido.getCliente().getId() == null) {
                throw new IllegalArgumentException("Cliente ID é obrigatório");
            }
            if (pedido.getRestaurante() == null || pedido.getRestaurante().getId() == null) {
                throw new IllegalArgumentException("Restaurante ID é obrigatório");
            }

            Long clienteId = pedido.getCliente().getId();
            Long restauranteId = pedido.getRestaurante().getId();

            Pedido pedidoSalvo = pedidoService.criar(pedido, clienteId, restauranteId);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    /**
     * Consultar pedidos por cliente
     */
    @GetMapping
    public ResponseEntity<List<Pedido>> consultarPorCliente(@RequestParam Long clienteId) {
        return ResponseEntity.ok(pedidoService.listarPorCliente(clienteId));
    }

    /**
     * Atualizar status do pedido
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Pedido pedido = pedidoService.atualizarStatus(id, status);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
}
