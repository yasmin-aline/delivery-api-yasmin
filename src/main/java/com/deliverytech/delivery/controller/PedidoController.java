package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.ItemPedidoDTO;
import com.deliverytech.delivery.dto.PedidoDTO;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Pedidos", description = "Operações relacionadas aos pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/api/pedidos")
    @Operation(summary = "Criar um novo pedido", description = "Valida cliente, restaurante, produtos e cria um novo pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada (ex: cliente inativo, produto indisponível)")
    })
    public ResponseEntity<Pedido> criarPedido(@Valid @RequestBody PedidoDTO dto) {
        Pedido pedidoSalvo = pedidoService.criarPedido(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
    }

    @GetMapping("/api/pedidos/{id}")
    @Operation(summary = "Buscar pedido por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<Pedido> buscarPedidoCompleto(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPedidoPorId(id);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/api/clientes/{clienteld}/pedidos")
    @Operation(summary = "Buscar histórico de pedidos de um cliente")
    public ResponseEntity<List<Pedido>> buscarPedidosDoCliente(@PathVariable Long clienteld) {
        return ResponseEntity.ok(pedidoService.buscarPedidosPorCliente(clienteld));
    }

    @PatchMapping("/api/pedidos/{id}/status")
    @Operation(summary = "Atualizar status de um pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status atualizado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "422", description = "Mudança de status inválida")
    })
    public ResponseEntity<Pedido> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        Pedido pedido = pedidoService.atualizarStatusPedido(id, status);
        return ResponseEntity.ok(pedido);
    }

    @DeleteMapping("/api/pedidos/{id}")
    @Operation(summary = "Cancelar um pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido cancelado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "422", description = "Pedido não pode ser cancelado")
    })
    public ResponseEntity<Pedido> cancelarPedido(@PathVariable Long id) {
        Pedido pedido = pedidoService.cancelarPedido(id);
        return ResponseEntity.ok(pedido);
    }

    @PostMapping("/api/pedidos/calcular")
    @Operation(summary = "Calcular total de um pedido (sem salvar)")
    public ResponseEntity<?> calcularTotal(@Valid @RequestBody List<ItemPedidoDTO> itens) {
        BigDecimal total = pedidoService.calcularTotalPedido(itens);
        return ResponseEntity.ok().body(java.util.Map.of("totalCalculado", total));
    }

    @GetMapping("/api/restaurantes/{restauranteld}/pedidos")
    @Operation(summary = "Buscar pedidos de um restaurante")
    public ResponseEntity<List<Pedido>> buscarPedidosDoRestaurante(@PathVariable Long restauranteld) {
        return ResponseEntity.ok(pedidoService.buscarPedidosPorRestaurante(restauranteld));
    }

    @GetMapping("/api/pedidos")
    @Operation(summary = "Listar pedidos com filtros", description = "Lista pedidos com filtros opcionais de status e período")
    public ResponseEntity<List<Pedido>> listarPedidos(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(pedidoService.buscarPedidosFiltrados(status, inicio, fim));
    }
}