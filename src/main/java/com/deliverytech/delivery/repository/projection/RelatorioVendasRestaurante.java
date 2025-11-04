package com.deliverytech.delivery.repository.projection;

import java.math.BigDecimal;

public interface RelatorioVendasRestaurante {

    String getNomeRestaurante();

    BigDecimal getTotalVendas();
}