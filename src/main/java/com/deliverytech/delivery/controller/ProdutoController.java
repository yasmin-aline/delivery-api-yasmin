package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.ProdutoDTO;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Produtos", description = "Operações relacionadas aos produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping("/api/produtos")
    @Operation(summary = "Cadastrar um novo produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: restaurante não existe, preço inválido)")
    })
    public ResponseEntity<Produto> cadastrar(@Valid @RequestBody ProdutoDTO dto) {
        Produto produtoSalvo = produtoService.cadastrarProduto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
    }

    @GetMapping("/api/produtos/{id}")
    @Operation(summary = "Buscar produto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        Produto produto = produtoService.buscarProdutoPorId(id);
        return ResponseEntity.ok(produto);
    }

    @GetMapping("/api/restaurantes/{restauranteld}/produtos")
    @Operation(summary = "Listar produtos de um restaurante", description = "Retorna todos os produtos disponíveis de um restaurante específico")
    public ResponseEntity<List<Produto>> buscarPorRestaurante(@PathVariable Long restauranteld) {
        return ResponseEntity.ok(produtoService.buscarProdutosPorRestaurante(restauranteld));
    }

    @PutMapping("/api/produtos/{id}")
    @Operation(summary = "Atualizar um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoDTO dto) {
        Produto produtoAtualizado = produtoService.atualizarProduto(id, dto);
        return ResponseEntity.ok(produtoAtualizado);
    }

    @PatchMapping("/api/produtos/{id}/disponibilidade")
    @Operation(summary = "Alterar disponibilidade de um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status alterado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Produto> alterarDisponibilidade(@PathVariable Long id, @RequestParam boolean disponivel) {
        Produto produto = produtoService.alterarDisponibilidade(id, disponivel);
        return ResponseEntity.ok(produto);
    }

    @GetMapping("/api/produtos/categoria/{categoria}")
    @Operation(summary = "Listar produtos por categoria")
    public ResponseEntity<List<Produto>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(produtoService.buscarProdutosPorCategoria(categoria));
    }

    @DeleteMapping("/api/produtos/{id}")
    @Operation(summary = "Remover um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto removido"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<?> removerProduto(@PathVariable Long id) {
        produtoService.removerProduto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/produtos/buscar")
    @Operation(summary = "Buscar produtos por nome", description = "Busca produtos que contenham o termo no nome")
    public ResponseEntity<List<Produto>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(produtoService.buscarProdutosPorNome(nome));
    }
}