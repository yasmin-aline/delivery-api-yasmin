package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.ClienteDTO;
import com.deliverytech.delivery.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    Cliente cadastrarCliente(ClienteDTO dto);
    Cliente buscarClientePorId(Long id);
    Optional<Cliente> buscarClientePorEmail(String email);
    Cliente atualizarCliente(Long id, ClienteDTO dto);
    Cliente ativarDesativarCliente(Long id);
    List<Cliente> listarClientesAtivos();
}