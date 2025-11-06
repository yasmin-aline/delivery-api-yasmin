package com.deliverytech.delivery.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RestauranteDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Categoria é obrigatória")
    private String categoria;

    @NotBlank(message = "Endereço é obrigatório")
    private String endereco;

    private String telefone;

    @DecimalMin(value = "0.0", message = "Taxa de entrega deve ser positiva")
    private BigDecimal taxaEntrega;

    private BigDecimal avaliacao;
}