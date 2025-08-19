// src/main/java/org/padaria/Main.java
package org.padaria;

import org.padaria.service.ClienteService;
import org.padaria.service.CompraService;
import org.padaria.service.FornecedorService;
import org.padaria.service.ProdutoService;
import org.padaria.service.VendaService;
import org.padaria.ui.MenuPrincipal;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String arquivoClientes = "clientes.csv";
        ClienteService clienteService = new ClienteService(arquivoClientes);
        CompraService compraService = new CompraService();
        FornecedorService fornecedorService = new FornecedorService();
        ProdutoService produtoService = new ProdutoService();
        VendaService vendaService = new VendaService();

        try {
            compraService.carregarDados("compras.csv");
            fornecedorService.carregarDados("fornecedores.csv");
            produtoService.carregarDados("produtos.csv");
            vendaService.carregarDados("vendas.csv");
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados iniciais: " + e.getMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);

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
            compraService.salvarDados("compras.csv");
            fornecedorService.salvarDados("fornecedores.csv");
            produtoService.salvarDados("produtos.csv");
            vendaService.salvarDados("vendas.csv");
            System.out.println("Dados salvos com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }
}