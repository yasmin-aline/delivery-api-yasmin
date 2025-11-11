package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.RestauranteDTO;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.service.RestauranteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/restaurantes")
@CrossOrigin(origins = "*")
@Tag(name = "Restaurantes", description = "Operações relacionadas aos restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    @PostMapping
    @Operation(summary = "Cadastrar um novo restaurante")
    @ApiResponses(value = @ApiResponse(responseCode = "201", description = "Restaurante criado"))
    public ResponseEntity<Restaurante> cadastrar(@Valid @RequestBody RestauranteDTO dto) {
        Restaurante restauranteSalvo = restauranteService.cadastrarRestaurante(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(restauranteSalvo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar restaurante por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<Restaurante> buscarPorId(@PathVariable Long id) {
        Restaurante restaurante = restauranteService.buscarRestaurantePorId(id);
        return ResponseEntity.ok(restaurante);
    }

    @GetMapping
    @Operation(summary = "Listar restaurantes com filtros", description = "Lista restaurantes, com filtros opcionais de categoria e status 'ativo'")
    public ResponseEntity<List<Restaurante>> listarRestaurantes(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Boolean ativo) {
        return ResponseEntity.ok(restauranteService.listarRestaurantesFiltrados(categoria, ativo));
    }

    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar restaurantes por categoria")
    public ResponseEntity<List<Restaurante>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(restauranteService.buscarRestaurantesPorCategoria(categoria));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurante atualizado"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<Restaurante> atualizar(@PathVariable Long id, @Valid @RequestBody RestauranteDTO dto) {
        Restaurante restauranteAtualizado = restauranteService.atualizarRestaurante(id, dto);
        return ResponseEntity.ok(restauranteAtualizado);
    }

    @GetMapping("/{id}/taxa-entrega/{cep}")
    @Operation(summary = "Calcular taxa de entrega", description = "Simula o cálculo da taxa de entrega para um CEP")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Taxa calculada"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<?> calcularTaxa(@PathVariable Long id, @PathVariable String cep) {
        BigDecimal taxa = restauranteService.calcularTaxaEntrega(id, cep);
        return ResponseEntity.ok().body(java.util.Map.of("taxaCalculada", taxa));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Ativar ou desativar um restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status alterado"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<Restaurante> ativarDesativarRestaurante(@PathVariable Long id) {
        Restaurante restaurante = restauranteService.ativarDesativarRestaurante(id);
        return ResponseEntity.ok(restaurante);
    }

    @GetMapping("/proximos/{cep}")
    @Operation(summary = "Buscar restaurantes próximos (Simulação)")
    public ResponseEntity<List<Restaurante>> buscarProximos(@PathVariable String cep) {
        return ResponseEntity.ok(restauranteService.buscarRestaurantesProximos(cep));
    }
}