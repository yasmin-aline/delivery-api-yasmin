package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.ProdutoDTO;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.service.ProdutoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping("/api/produtos")
    public ResponseEntity<?> cadastrar(@Valid @RequestBody ProdutoDTO dto) {
        try {
            Produto produtoSalvo = produtoService.cadastrarProduto(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("/api/produtos/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Produto produto = produtoService.buscarProdutoPorId(id);
            return ResponseEntity.ok(produto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/restaurantes/{restauranteld}/produtos")
    public ResponseEntity<List<Produto>> buscarPorRestaurante(@PathVariable Long restauranteld) {
        return ResponseEntity.ok(produtoService.buscarProdutosPorRestaurante(restauranteld));
    }

    @PutMapping("/api/produtos/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoDTO dto) {
        try {
            Produto produtoAtualizado = produtoService.atualizarProduto(id, dto);
            return ResponseEntity.ok(produtoAtualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @PatchMapping("/api/produtos/{id}/disponibilidade")
    public ResponseEntity<?> alterarDisponibilidade(@PathVariable Long id, @RequestParam boolean disponivel) {
        try {
            Produto produto = produtoService.alterarDisponibilidade(id, disponivel);
            return ResponseEntity.ok(produto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/produtos/categoria/{categoria}")
    public ResponseEntity<List<Produto>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(produtoService.buscarProdutosPorCategoria(categoria));
    }
}