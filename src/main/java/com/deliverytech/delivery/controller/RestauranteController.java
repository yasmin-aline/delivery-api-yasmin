package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.RestauranteDTO;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.service.RestauranteService;
import jakarta.persistence.EntityNotFoundException;
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
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody RestauranteDTO dto) {
        try {
            Restaurante restauranteSalvo = restauranteService.cadastrarRestaurante(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(restauranteSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Restaurante restaurante = restauranteService.buscarRestaurantePorId(id);
            return ResponseEntity.ok(restaurante);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Restaurante>> listarDisponiveis() {
        return ResponseEntity.ok(restauranteService.buscarRestaurantesDisponiveis());
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Restaurante>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(restauranteService.buscarRestaurantesPorCategoria(categoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody RestauranteDTO dto) {
        try {
            Restaurante restauranteAtualizado = restauranteService.atualizarRestaurante(id, dto);
            return ResponseEntity.ok(restauranteAtualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/taxa-entrega/{cep}")
    public ResponseEntity<?> calcularTaxa(@PathVariable Long id, @PathVariable String cep) {
        try {
            BigDecimal taxa = restauranteService.calcularTaxaEntrega(id, cep);
            return ResponseEntity.ok().body(java.util.Map.of("taxaCalculada", taxa));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}