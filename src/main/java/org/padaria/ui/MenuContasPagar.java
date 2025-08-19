package org.padaria.ui;

import org.padaria.model.Compra;
import org.padaria.model.Fornecedor;
import org.padaria.model.Produto;
import org.padaria.service.CompraService;
import org.padaria.service.FornecedorService;
import org.padaria.service.ProdutoService;
import org.padaria.util.MenuUtil;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MenuContasPagar {

    private final CompraService compraService;
    private final FornecedorService fornecedorService;
    private final ProdutoService produtoService;

    public MenuContasPagar(CompraService compraService, FornecedorService fornecedorService,
            ProdutoService produtoService) {
        this.compraService = compraService;
        this.fornecedorService = fornecedorService;
        this.produtoService = produtoService;
    }

    public void exibirMenu(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n--- CONTAS A PAGAR ---");
            System.out.println("1. Visualizar Contas a Pagar por Fornecedor");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = MenuUtil.lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    visualizarContasPagarPorFornecedor();
                    break;
                case 0:
                    System.out.println("Voltando ao menu anterior.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }

    private void visualizarContasPagarPorFornecedor() {
        System.out.println("\n--- TOTAL A PAGAR POR FORNECEDOR ---");

        Map<Integer, List<Compra>> comprasPorFornecedor = compraService.listar().stream()
                .collect(Collectors.groupingBy(Compra::getCodigoFornecedor));

        if (comprasPorFornecedor.isEmpty()) {
            System.out.println("Nenhuma conta a pagar encontrada.");
            return;
        }

        for (Map.Entry<Integer, List<Compra>> entry : comprasPorFornecedor.entrySet()) {
            int codigoFornecedor = entry.getKey();
            List<Compra> compras = entry.getValue();
            Fornecedor fornecedor = fornecedorService.buscar(codigoFornecedor);

            double totalPagar = 0;
            System.out.println("--------------------------------------------------");
            System.out.printf("Fornecedor: %s (Cód: %d)%n", fornecedor != null ? fornecedor.getNome() : "Desconhecido",
                    codigoFornecedor);

            for (Compra compra : compras) {
                Produto produto = produtoService.buscar(compra.getCodigoProduto());
                double valorTotalItem = 0;
                if (produto != null) {
                    valorTotalItem = produto.getValorCusto() * compra.getQuantidade();
                }
                totalPagar += valorTotalItem;

                System.out.printf("  - Produto: %s (Cód: %d), Quantidade: %d, Valor: R$ %.2f%n",
                        produto != null ? produto.getDescricao() : "Desconhecido",
                        compra.getCodigoProduto(),
                        compra.getQuantidade(),
                        valorTotalItem);
            }
            System.out.printf("  Total a pagar: R$ %.2f%n", totalPagar);
        }
    }
}