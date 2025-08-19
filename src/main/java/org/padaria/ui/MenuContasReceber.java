package org.padaria.ui;

import org.padaria.model.Cliente;
import org.padaria.model.ModoPagamento;
import org.padaria.model.Venda;
import org.padaria.model.Produto;
import org.padaria.service.ClienteService;
import org.padaria.service.VendaService;
import org.padaria.service.ProdutoService;
import org.padaria.util.MenuUtil;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MenuContasReceber {

    private final VendaService vendaService;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;

    public MenuContasReceber(VendaService vendaService, ClienteService clienteService, ProdutoService produtoService) {
        this.vendaService = vendaService;
        this.clienteService = clienteService;
        this.produtoService = produtoService;
    }

    public void exibirMenu(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n--- CONTAS A RECEBER ---");
            System.out.println("1. Visualizar Contas a Receber por Cliente");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = MenuUtil.lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    visualizarContasReceberPorCliente();
                    break;
                case 0:
                    System.out.println("Voltando ao menu anterior.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }

    private void visualizarContasReceberPorCliente() {
        System.out.println("\n--- TOTAL A RECEBER POR CLIENTE ---");

        Map<Integer, List<Venda>> vendasPorCliente = vendaService.listar().stream()
                .filter(venda -> venda.getModoPagamento() == ModoPagamento.FIADO)
                .collect(Collectors.groupingBy(Venda::getCodigoCliente));

        if (vendasPorCliente.isEmpty()) {
            System.out.println("Nenhuma conta a receber encontrada.");
            return;
        }

        for (Map.Entry<Integer, List<Venda>> entry : vendasPorCliente.entrySet()) {
            int codigoCliente = entry.getKey();
            List<Venda> vendas = entry.getValue();
            Cliente cliente = clienteService.buscar(codigoCliente);

            double totalReceber = 0;
            System.out.println("--------------------------------------------------");
            System.out.printf("Cliente: %s (Cód: %d)%n", cliente != null ? cliente.getNome() : "Desconhecido",
                    codigoCliente);

            for (Venda venda : vendas) {
                Produto produto = produtoService.buscar(venda.getCodigoProduto());
                double valorTotalItem = 0;
                if (produto != null) {
                    valorTotalItem = produto.calcularValorVenda() * venda.getQuantidade();
                }
                totalReceber += valorTotalItem;

                System.out.printf("  - Produto: %s (Cód: %d), Quantidade: %d, Valor: R$ %.2f%n",
                        produto != null ? produto.getDescricao() : "Desconhecido",
                        venda.getCodigoProduto(),
                        venda.getQuantidade(),
                        valorTotalItem);
            }
            System.out.printf("  Total a receber: R$ %.2f%n", totalReceber);
        }
    }
}