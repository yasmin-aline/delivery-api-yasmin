package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.ProdutoDTO;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.exception.EntityNotFoundException;
import com.deliverytech.delivery.repository.ProdutoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public Produto cadastrarProduto(ProdutoDTO dto) {
        Restaurante restaurante = restauranteService.buscarRestaurantePorId(dto.getRestauranteId());
        Produto produto = modelMapper.map(dto, Produto.class);
        produto.setRestaurante(restaurante);
        if (produto.getDisponivel() == null) {
            produto.setDisponivel(true);
        }
        return produtoRepository.save(produto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produto> buscarProdutosPorRestaurante(Long restauranteId) {
        return produtoRepository.findByRestauranteIdAndDisponivelTrue(restauranteId);
    }

    @Override
    @Transactional(readOnly = true)
    public Produto buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + id));
    }

    @Override
    @Transactional
    public Produto atualizarProduto(Long id, ProdutoDTO dto) {
        Produto produto = buscarProdutoPorId(id);
        Restaurante restaurante = restauranteService.buscarRestaurantePorId(dto.getRestauranteId());
        modelMapper.map(dto, produto);
        produto.setId(id);
        produto.setRestaurante(restaurante);
        return produtoRepository.save(produto);
    }

    @Override
    @Transactional
    public Produto alterarDisponibilidade(Long id, boolean disponivel) {
        Produto produto = buscarProdutoPorId(id);
        produto.setDisponivel(disponivel);
        return produtoRepository.save(produto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produto> buscarProdutosPorCategoria(String categoria) {
        return produtoRepository.findByCategoriaContainingIgnoreCase(categoria);
    }

    @Override
    @Transactional
    public void removerProduto(Long id) {
        Produto produto = buscarProdutoPorId(id);
        produtoRepository.delete(produto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produto> buscarProdutosPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }
}