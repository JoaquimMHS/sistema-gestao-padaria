package org.padaria.ui;

import org.padaria.service.ClienteService;
import org.padaria.service.FornecedorService;
import org.padaria.service.ProdutoService;
import org.padaria.util.MenuUtil;

import java.util.Scanner;

public class MenuCadastro {

    private final ClienteService clienteService;
    private final FornecedorService fornecedorService;
    private final ProdutoService produtoService;

    public MenuCadastro(ClienteService clienteService, FornecedorService fornecedorService,
            ProdutoService produtoService) {
        this.clienteService = clienteService;
        this.fornecedorService = fornecedorService;
        this.produtoService = produtoService;
    }

    public void exibirMenu(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n--- MENU DE CADASTRO ---");
            System.out.println("1. Clientes");
            System.out.println("2. Fornecedores");
            System.out.println("3. Produtos");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = MenuUtil.lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    MenuCliente menuCliente = new MenuCliente(clienteService);
                    menuCliente.exibirMenu(scanner);
                    break;
                case 2:
                    MenuFornecedor menuFornecedor = new MenuFornecedor(fornecedorService);
                    menuFornecedor.exibirMenu(scanner);
                    break;
                case 3:
                    MenuProduto menuProduto = new MenuProduto(produtoService);
                    menuProduto.exibirMenu(scanner);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }
}