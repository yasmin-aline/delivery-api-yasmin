package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.ClienteDTO;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public Cliente cadastrarCliente(ClienteDTO dto) {
        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado: " + dto.getEmail());
        }

        Cliente cliente = modelMapper.map(dto, Cliente.class);

        cliente.setAtivo(true);
        cliente.setDataCadastro(LocalDateTime.now());

        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarClientePorEmail(String email) {
        return clienteRepository.findByEmail(email); //
    }

    @Override
    @Transactional
    public Cliente atualizarCliente(Long id, ClienteDTO dto) {
        Cliente cliente = buscarClientePorId(id);

        if (!cliente.getEmail().equals(dto.getEmail()) && clienteRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado: " + dto.getEmail());
        }

        modelMapper.map(dto, cliente);
        cliente.setId(id);

        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public Cliente ativarDesativarCliente(Long id) {
        Cliente cliente = buscarClientePorId(id);

        cliente.setAtivo(!cliente.getAtivo());

        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarClientesAtivos() {
        return clienteRepository.findByAtivoTrue(); //
    }
}