package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.ProdutoDTO;
import com.deliverytech.delivery.entity.Produto;
import java.util.List;

public interface ProdutoService {

    Produto cadastrarProduto(ProdutoDTO dto);

    List<Produto> buscarProdutosPorRestaurante(Long restauranteId);

    Produto buscarProdutoPorId(Long id);

    Produto atualizarProduto(Long id, ProdutoDTO dto);

    Produto alterarDisponibilidade(Long id, boolean disponivel);

    List<Produto> buscarProdutosPorCategoria(String categoria);

    void removerProduto(Long id);

    List<Produto> buscarProdutosPorNome(String nome);
}