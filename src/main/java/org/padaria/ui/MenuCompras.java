package org.padaria.ui;

import org.padaria.model.Compra;
import org.padaria.service.CompraService;
import org.padaria.service.FornecedorService;
import org.padaria.service.ProdutoService;
import org.padaria.util.MenuUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MenuCompras {

    private final CompraService compraService;
    private final FornecedorService fornecedorService;
    private final ProdutoService produtoService;

    public MenuCompras(CompraService compraService, FornecedorService fornecedorService,
            ProdutoService produtoService) {
        this.compraService = compraService;
        this.fornecedorService = fornecedorService;
        this.produtoService = produtoService;
    }

    public void exibirMenu(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n--- MENU DE COMPRAS ---");
            System.out.println("1. Listar Todas as Compras");
            System.out.println("2. Adicionar Nova Compra");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = MenuUtil.lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    listarCompras();
                    break;
                case 2:
                    adicionarCompra(scanner);
                    break;
                case 0:
                    System.out.println("Voltando ao menu anterior.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }

    private void listarCompras() {
        List<Compra> compras = compraService.listar();
        if (compras.isEmpty()) {
            System.out.println("Nenhuma compra registrada.");
            return;
        }
        System.out.println("\n--- LISTA DE COMPRAS ---");
        System.out.printf("%-10s | %-15s | %-15s | %-15s | %-10s%n",
                "Nº NF", "Cód. Fornecedor", "Data Compra", "Cód. Produto", "Quantidade");
        System.out.println("----------------------------------------------------------------------");
        for (Compra c : compras) {
            System.out.printf("%-10d | %-15d | %-15s | %-15d | %-10d%n",
                    c.getNumeroNotaFiscal(), c.getCodigoFornecedor(), c.getDataCompra(),
                    c.getCodigoProduto(), c.getQuantidade());
        }
    }

    private void adicionarCompra(Scanner scanner) {
        System.out.println("\n--- ADICIONAR NOVA COMPRA ---");
        System.out.print("Número da Nota Fiscal: ");
        int numeroNF = MenuUtil.lerOpcao(scanner);

        System.out.print("Código do Fornecedor: ");
        int codigoFornecedor = MenuUtil.lerOpcao(scanner);
        if (fornecedorService.buscar(codigoFornecedor) == null) {
            System.out.println("Fornecedor não encontrado. Compra não registrada.");
            return;
        }

        System.out.print("Código do Produto: ");
        int codigoProduto = MenuUtil.lerOpcao(scanner);
        if (produtoService.buscar(codigoProduto) == null) {
            System.out.println("Produto não encontrado. Compra não registrada.");
            return;
        }

        System.out.print("Quantidade: ");
        int quantidade = MenuUtil.lerOpcao(scanner);

        LocalDate dataAtual = LocalDate.now();
        Compra novaCompra = new Compra(numeroNF, codigoFornecedor, dataAtual, codigoProduto, quantidade);
        compraService.cadastrar(novaCompra);
        System.out.println("Compra registrada com sucesso!");
    }
}