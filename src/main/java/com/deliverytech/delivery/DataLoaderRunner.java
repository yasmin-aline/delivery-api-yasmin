package com.deliverytech.delivery;

import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.repository.projection.RelatorioVendasRestaurante;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

//@Component
public class DataLoaderRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoaderRunner.class);

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public void run(String... args) throws Exception {

        // Populando Restaurantes
        Restaurante r1 = new Restaurante(null, "Pizzaria da Ana", "Pizza", "Rua A, 10", "1111-1111", new BigDecimal("5.00"), new BigDecimal("4.5"), true);
        Restaurante r2 = new Restaurante(null, "Burger do Bruno", "Lanche", "Rua B, 20", "2222-2222", new BigDecimal("3.50"), new BigDecimal("4.2"), true);
        restauranteRepository.saveAll(List.of(r1, r2));
        logger.info("Restaurantes salvos.");

        // Populando Clientes
        Cliente c1 = new Cliente(null, "Ana", "ana@email.com", "3333-3333", "Rua C, 30", LocalDateTime.now(), true);
        Cliente c2 = new Cliente(null, "Bruno", "bruno@email.com", "4444-4444", "Rua D, 40", LocalDateTime.now(), true);
        Cliente c3 = new Cliente(null, "Carla", "carla@email.com", "5555-5555", "Rua E, 50", LocalDateTime.now(), false); // Cliente inativo
        clienteRepository.saveAll(List.of(c1, c2, c3));
        logger.info("Clientes salvos.");

        // Populando Produtos
        Produto p1 = new Produto(null, "Pizza Muçarela", "Queijo e tomate", new BigDecimal("30.00"), "Pizza", true, r1);
        Produto p2 = new Produto(null, "Pizza Calabresa", "Queijo e calabresa", new BigDecimal("35.00"), "Pizza", true, r1);
        Produto p3 = new Produto(null, "X-Burger", "Pão, carne, queijo", new BigDecimal("20.00"), "Lanche", true, r2);
        Produto p4 = new Produto(null, "X-Bacon", "Pão, carne, queijo, bacon", new BigDecimal("25.00"), "Lanche", true, r2);
        Produto p5 = new Produto(null, "Batata Frita", "Porção", new BigDecimal("10.00"), "Acompanhamento", false, r2); // Produto indisponível
        produtoRepository.saveAll(List.of(p1, p2, p3, p4, p5));
        logger.info("Produtos salvos.");

        // Populando Pedidos
        Pedido ped1 = new Pedido(null, "PED-001", LocalDateTime.now().minusDays(1), "ENTREGUE", new BigDecimal("30.00"), "Sem cebola", "1x Pizza Muçarela", c1, r1);
        Pedido ped2 = new Pedido(null, "PED-002", LocalDateTime.now(), "PENDENTE", new BigDecimal("45.00"), "Capricha no bacon", "1x X-Bacon, 1x Batata", c2, r2);
        pedidoRepository.saveAll(List.of(ped1, ped2));
        logger.info("Pedidos salvos.");


        logger.info("Validação -------------");

        logger.info("[Cliente] findByEmail('ana@email.com'): {}", clienteRepository.findByEmail("ana@email.com").isPresent());
        logger.info("[Cliente] findByAtivoTrue() (Esperado: 2): {}", clienteRepository.findByAtivoTrue().size());
        logger.info("[Cliente] findByNomeContainingIgnoreCase('bruno') (Esperado: 1): {}", clienteRepository.findByNomeContainingIgnoreCase("bruno").size());
        logger.info("[Cliente] existsByEmail('ana@email.com') (Esperado: true): {}", clienteRepository.existsByEmail("ana@email.com"));
        logger.info("[Cliente] existsByEmail('naoexiste@email.com') (Esperado: false): {}", clienteRepository.existsByEmail("naoexiste@email.com"));

        logger.info("[Restaurante] findByCategoriaContainingIgnoreCase('pizza') (Esperado: 1): {}", restauranteRepository.findByCategoriaContainingIgnoreCase("pizza").size());
        logger.info("[Restaurante] findByTaxaEntregaLessThanEqual(4.00) (Esperado: 1): {}", restauranteRepository.findByTaxaEntregaLessThanEqual(new BigDecimal("4.00")).size()); //
        logger.info("[Restaurante] findTop5ByOrderByNomeAsc() (Primeiro deve ser 'Burger do Bruno'): {}", restauranteRepository.findTop5ByOrderByNomeAsc().get(0).getNome());

        logger.info("[Produto] findByRestauranteId(ID do Burger) (Esperado: 3): {}", produtoRepository.findByRestauranteId(r2.getId()).size()); //
        logger.info("[Produto] findByDisponivelTrue() (Esperado: 4): {}", produtoRepository.findByDisponivelTrue().size());
        logger.info("[Produto] findByCategoria('Lanche') (Esperado: 2): {}", produtoRepository.findByCategoria("Lanche").size());
        logger.info("[Produto] findByPrecoLessThanEqual(20.00) (Esperado: 2): {}", produtoRepository.findByPrecoLessThanEqual(new BigDecimal("20.00")).size());

        logger.info("[Pedido] findByClienteId(ID da Ana) (Esperado: 1): {}", pedidoRepository.findByClienteId(c1.getId()).size());
        logger.info("[Pedido] findByStatus('ENTREGUE') (Esperado: 1): {}", pedidoRepository.findByStatus("ENTREGUE").size());
        logger.info("[Pedido] findTop10ByOrderByDataPedidoDesc() (Primeiro deve ser PED-002): {}", pedidoRepository.findTop10ByOrderByDataPedidoDesc().get(0).getNumeroPedido()); //
        logger.info("[Pedido] findByDataPedidoBetween(Ontem, Amanhã) (Esperado: 2): {}", pedidoRepository.findByDataPedidoBetween(LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(1)).size());

        logger.info("--- 3. Validando consultas @Query (Atividade 3) ---");


        List<Pedido> pedidosCaros = pedidoRepository.findPedidosComValorAcimaDe(new BigDecimal("40.00"));
        logger.info("[@Query 1] findPedidosComValorAcimaDe(40.00) (Esperado: 1): {}", pedidosCaros.size());


        List<Pedido> relatorio = pedidoRepository.findRelatorioPorPeriodoEStatus(
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now(),
                "ENTREGUE"
        );
        logger.info("[@Query 2] findRelatorioPorPeriodoEStatus('ENTREGUE') (Esperado: 1): {}", relatorio.size());

        logger.info("[@Query 3] findTotalVendasPorRestaurante():");
        List<RelatorioVendasRestaurante> vendas = pedidoRepository.findTotalVendasPorRestaurante();

        for (RelatorioVendasRestaurante item : vendas) {
            logger.info("  -> Restaurante: {}, Total: R$ {}", item.getNomeRestaurante(), item.getTotalVendas());
        }
    }
}