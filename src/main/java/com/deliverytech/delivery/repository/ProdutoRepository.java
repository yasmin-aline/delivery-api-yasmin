package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Buscar produtos por restaurante
    List<Produto> findByRestauranteId(Long restauranteId);

    // Buscar produtos por restaurante e disponibilidade
    List<Produto> findByRestauranteIdAndDisponivelTrue(Long restauranteId);

    // Buscar produtos por categoria
    List<Produto> findByCategoriaContainingIgnoreCase(String categoria);
}
