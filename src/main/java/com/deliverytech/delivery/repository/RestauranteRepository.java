package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    // Buscar por nome
    List<Restaurante> findByNomeContainingIgnoreCase(String nome);

    // Buscar por categoria
    List<Restaurante> findByCategoriaContainingIgnoreCase(String categoria);

    // Buscar ativos
    List<Restaurante> findByAtivoTrue();

    // Buscar ativos e ordenar por avaliação
    List<Restaurante> findByAtivoTrueOrderByAvaliacaoDesc();

    // Buscar por taxa de entrega menor ou igual (taxa)
    List<Restaurante> findByTaxaEntregaLessThanEqual(java.math.BigDecimal taxa);

    //  Order by nome asc dos restaurantes
    List<Restaurante> findTop5ByOrderByNomeAsc();
}
