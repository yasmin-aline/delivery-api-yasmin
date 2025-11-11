package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.RestauranteDTO;
import com.deliverytech.delivery.entity.Restaurante;
import java.math.BigDecimal;
import java.util.List;

public interface RestauranteService {

    Restaurante cadastrarRestaurante(RestauranteDTO dto);
    Restaurante buscarRestaurantePorId(Long id);
    List<Restaurante> buscarRestaurantesPorCategoria(String categoria);
    List<Restaurante> buscarRestaurantesDisponiveis();
    Restaurante atualizarRestaurante(Long id, RestauranteDTO dto);
    BigDecimal calcularTaxaEntrega(Long restauranteId, String cep);
    Restaurante ativarDesativarRestaurante(Long id);
    List<Restaurante> buscarRestaurantesProximos(String cep);
    List<Restaurante> listarRestaurantesFiltrados(String categoria, Boolean ativo);
}