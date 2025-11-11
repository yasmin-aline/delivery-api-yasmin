package com.deliverytech.delivery.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RestauranteDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Categoria é obrigatória")
    private String categoria;

    @NotBlank(message = "Endereço é obrigatório")
    private String endereco;

    private String telefone;

    @NotNull(message = "Taxa de entrega é obrigatória")
    @DecimalMin(value = "0.0", message = "Taxa de entrega não pode ser negativa")
    private BigDecimal taxaEntrega;

    @NotNull(message = "Tempo de entrega é obrigatório")
    @Min(value = 10, message = "Tempo de entrega deve ser de no mínimo 10 minutos")
    @Max(value = 120, message = "Tempo de entrega deve ser de no máximo 120 minutos")
    private Integer tempoEntrega;

    private BigDecimal avaliacao;
}