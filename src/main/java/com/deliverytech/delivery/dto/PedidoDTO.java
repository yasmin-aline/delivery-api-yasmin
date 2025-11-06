package com.deliverytech.delivery.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class PedidoDTO {

    @NotNull(message = "ID do Cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "ID do Restaurante é obrigatório")
    private Long restauranteId;

    @NotBlank(message = "Endereço de entrega é obrigatório")
    private String enderecoEntrega;

    private String observacoes;

    @NotEmpty(message = "Pedido deve conter pelo menos um item")
    @Valid
    private List<ItemPedidoDTO> itens;
}