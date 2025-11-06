package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.RestauranteDTO;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.repository.RestauranteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class RestauranteServiceImpl implements RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public Restaurante cadastrarRestaurante(RestauranteDTO dto) {
        if (dto.getTaxaEntrega() == null || dto.getTaxaEntrega().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Taxa de entrega deve ser zero ou positiva");
        }

        Restaurante restaurante = modelMapper.map(dto, Restaurante.class);
        restaurante.setAtivo(true);

        return restauranteRepository.save(restaurante);
    }

    @Override
    @Transactional(readOnly = true)
    public Restaurante buscarRestaurantePorId(Long id) {
        return restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurante> buscarRestaurantesPorCategoria(String categoria) {
        return restauranteRepository.findByCategoriaContainingIgnoreCase(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurante> buscarRestaurantesDisponiveis() {
        return restauranteRepository.findByAtivoTrue();
    }

    @Override
    @Transactional
    public Restaurante atualizarRestaurante(Long id, RestauranteDTO dto) {
        Restaurante restaurante = buscarRestaurantePorId(id);

        modelMapper.map(dto, restaurante);
        restaurante.setId(id);

        return restauranteRepository.save(restaurante);
    }

    @Override
    public BigDecimal calcularTaxaEntrega(Long restauranteId, String cep) {
        Restaurante restaurante = buscarRestaurantePorId(restauranteId);

        // Simulação: se o CEP começar com "0", a taxa é maior
        if (cep != null && cep.startsWith("0")) {
            return restaurante.getTaxaEntrega().add(new BigDecimal("2.00"));
        }

        return restaurante.getTaxaEntrega();
    }
}