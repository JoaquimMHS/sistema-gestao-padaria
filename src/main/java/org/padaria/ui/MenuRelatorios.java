package org.padaria.ui;

import org.padaria.report.RelatorioContasPagar;
import org.padaria.report.RelatorioContasReceber;
import org.padaria.report.RelatorioEstoque;
import org.padaria.report.RelatorioVendasPagamento;
import org.padaria.report.RelatorioVendasProduto;
import org.padaria.service.ClienteService;
import org.padaria.service.CompraService;
import org.padaria.service.FornecedorService;
import org.padaria.service.ProdutoService;
import org.padaria.service.VendaService;
import org.padaria.util.MenuUtil;

import java.io.IOException;
import java.util.Scanner;

public class MenuRelatorios {

    private final VendaService vendaService;
    private final ProdutoService produtoService;
    private final ClienteService clienteService;
    private final CompraService compraService;
    private final FornecedorService fornecedorService;

    public MenuRelatorios(VendaService vendaService, ProdutoService produtoService, ClienteService clienteService,
            CompraService compraService, FornecedorService fornecedorService) {
        this.vendaService = vendaService;
        this.produtoService = produtoService;
        this.clienteService = clienteService;
        this.compraService = compraService;
        this.fornecedorService = fornecedorService;
    }

    public void exibirMenu(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n--- GERAÇÃO DE RELATÓRIOS ---");
            System.out.println("1. Gerar todos os relatórios");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = MenuUtil.lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    gerarTodosRelatorios();
                    break;
                case 0:
                    System.out.println("Voltando ao menu anterior.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }

    private void gerarTodosRelatorios() {
        System.out.println("Iniciando a geração de relatórios...");
        try {
            // Relatório de Contas a Pagar
            RelatorioContasPagar relatorioAPagar = new RelatorioContasPagar(fornecedorService, compraService,
                    produtoService);
            relatorioAPagar.gerar("1-apagar.csv");
            System.out.println("-> Relatório de Contas a Pagar salvo em 1-apagar.csv");

            // Relatório de Contas a Receber
            RelatorioContasReceber relatorioAReceber = new RelatorioContasReceber(clienteService, vendaService,
                    produtoService);
            relatorioAReceber.gerar("2-areceber.csv");
            System.out.println("-> Relatório de Contas a Receber salvo em 2-areceber.csv");

            // Relatório de Vendas por Produto
            RelatorioVendasProduto relatorioVendasProduto = new RelatorioVendasProduto(vendaService, produtoService);
            relatorioVendasProduto.gerar("3-vendasprod.csv");
            System.out.println("-> Relatório de Vendas por Produto salvo em 3-vendasprod.csv");

            // Relatório de Vendas por Pagamento
            RelatorioVendasPagamento relatorioVendasPagamento = new RelatorioVendasPagamento(vendaService,
                    produtoService);
            relatorioVendasPagamento.gerar("4-vendaspgto.csv");
            System.out.println("-> Relatório de Vendas por Pagamento salvo em 4-vendaspgto.csv");

            // Relatório de Estoque
            RelatorioEstoque relatorioEstoque = new RelatorioEstoque(produtoService);
            relatorioEstoque.gerar("5-estoque.csv");
            System.out.println("-> Relatório de Estoque salvo em 5-estoque.csv");

            System.out.println("Todos os relatórios foram gerados com sucesso!");

        } catch (IOException e) {
            System.err.println("Erro ao gerar relatórios: " + e.getMessage());
        }
    }
}