// src/main/java/org/padaria/Main.java
package org.padaria;

import org.padaria.service.ClienteService;
import org.padaria.service.CompraService;
import org.padaria.service.FornecedorService;
import org.padaria.service.ProdutoService;
import org.padaria.service.VendaService;
import org.padaria.ui.MenuPrincipal;

import java.util.Scanner;

public class NewMain {
    /**
     * Essa clase inicia o programa com interface de linha de comando pedindo os
     * nomes dos arquivos
     * 
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Pedir os nomes dos arquivos ao usuário
        System.out.println("=== CONFIGURAÇÃO DE ARQUIVOS ===");
        System.out.print("Digite o nome do arquivo de clientes (padrão: clientes.csv): ");
        String arquivoClientes = scanner.nextLine().trim();
        if (arquivoClientes.isEmpty()) {
            arquivoClientes = "clientes.csv";
        }

        System.out.print("Digite o nome do arquivo de produtos (padrão: produtos.csv): ");
        String arquivoProdutos = scanner.nextLine().trim();
        if (arquivoProdutos.isEmpty()) {
            arquivoProdutos = "produtos.csv";
        }

        System.out.print("Digite o nome do arquivo de fornecedores (padrão: fornecedores.csv): ");
        String arquivoFornecedores = scanner.nextLine().trim();
        if (arquivoFornecedores.isEmpty()) {
            arquivoFornecedores = "fornecedores.csv";
        }

        System.out.print("Digite o nome do arquivo de compras (padrão: compras.csv): ");
        String arquivoCompras = scanner.nextLine().trim();
        if (arquivoCompras.isEmpty()) {
            arquivoCompras = "compras.csv";
        }

        System.out.print("Digite o nome do arquivo de vendas (padrão: vendas.csv): ");
        String arquivoVendas = scanner.nextLine().trim();
        if (arquivoVendas.isEmpty()) {
            arquivoVendas = "vendas.csv";
        }

        System.out.println("\nArquivos configurados:");
        System.out.println("Clientes: " + arquivoClientes);
        System.out.println("Produtos: " + arquivoProdutos);
        System.out.println("Fornecedores: " + arquivoFornecedores);
        System.out.println("Compras: " + arquivoCompras);
        System.out.println("Vendas: " + arquivoVendas);
        System.out.println("==================\n");

        // Inicializar serviços com os arquivos informados
        ClienteService clienteService = new ClienteService(arquivoClientes);
        CompraService compraService = new CompraService();
        FornecedorService fornecedorService = new FornecedorService();
        ProdutoService produtoService = new ProdutoService();
        VendaService vendaService = new VendaService();

        try {
            compraService.carregarDados(arquivoCompras);
            fornecedorService.carregarDados(arquivoFornecedores);
            produtoService.carregarDados(arquivoProdutos);
            vendaService.carregarDados(arquivoVendas);
            System.out.println("Dados carregados com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados iniciais: " + e.getMessage());
            System.out.println("Deseja continuar mesmo com erro? (S/N)");
            String continuar = scanner.nextLine().trim();
            if (!continuar.equalsIgnoreCase("S")) {
                System.out.println("Encerrando programa...");
                return;
            }
        }

        MenuPrincipal menuPrincipal = new MenuPrincipal(
                compraService,
                fornecedorService,
                produtoService,
                vendaService,
                clienteService,
                scanner);
        menuPrincipal.exibirMenu();

        try {
            clienteService.salvarClientes();
            compraService.salvarDados(arquivoCompras);
            fornecedorService.salvarDados(arquivoFornecedores);
            produtoService.salvarDados(arquivoProdutos);
            vendaService.salvarDados(arquivoVendas);
            System.out.println("Dados salvos com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }

        scanner.close();
    }
}