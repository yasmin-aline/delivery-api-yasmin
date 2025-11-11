package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.RestauranteDTO;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.exception.BusinessException;
import com.deliverytech.delivery.exception.EntityNotFoundException;
import com.deliverytech.delivery.repository.RestauranteRepository;
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
            throw new BusinessException("Taxa de entrega deve ser zero ou positiva");
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
        if (cep != null && cep.startsWith("0")) {
            return restaurante.getTaxaEntrega().add(new BigDecimal("2.00"));
        }
        return restaurante.getTaxaEntrega();
    }

    @Override
    @Transactional
    public Restaurante ativarDesativarRestaurante(Long id) {
        Restaurante restaurante = buscarRestaurantePorId(id);
        restaurante.setAtivo(!restaurante.getAtivo());
        return restauranteRepository.save(restaurante);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurante> buscarRestaurantesProximos(String cep) {
        return restauranteRepository.findByAtivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurante> listarRestaurantesFiltrados(String categoria, Boolean ativo) {
        if (categoria != null && ativo != null) {
            return restauranteRepository.findByCategoriaContainingIgnoreCase(categoria)
                    .stream()
                    .filter(r -> r.getAtivo().equals(ativo))
                    .toList();
        }
        if (categoria != null) {
            return restauranteRepository.findByCategoriaContainingIgnoreCase(categoria);
        }
        if (ativo != null) {
            return restauranteRepository.findByAtivoTrue();
        }
        return restauranteRepository.findAll();
    }
}