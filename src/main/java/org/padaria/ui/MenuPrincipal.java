package org.padaria.ui;

import org.padaria.service.*;
import org.padaria.util.MenuUtil;

import java.util.Scanner;

public class MenuPrincipal {
    private final CompraService compraService;
    private final FornecedorService fornecedorService;
    private final ProdutoService produtoService;
    private final VendaService vendaService;
    private final ClienteService clienteService;
    private final Scanner scanner;

    public MenuPrincipal(CompraService compraService, FornecedorService fornecedorService,
            ProdutoService produtoService, VendaService vendaService, ClienteService clienteService, Scanner scanner) {
        this.compraService = compraService;
        this.fornecedorService = fornecedorService;
        this.produtoService = produtoService;
        this.vendaService = vendaService;
        this.clienteService = clienteService;
        this.scanner = scanner;
    }

    public void exibirMenu() {
        int opcao;
        do {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Cadastro");
            System.out.println("2. Registro de Vendas");
            System.out.println("3. Controle de Contas");
            System.out.println("4. Geração de Relatórios Mensais");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = MenuUtil.lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    MenuCadastro menuCadastro = new MenuCadastro(clienteService, fornecedorService, produtoService);
                    menuCadastro.exibirMenu(scanner);
                    break;
                case 2:
                    MenuVendas menuVendas = new MenuVendas(vendaService, scanner);
                    menuVendas.exibirMenu();
                    break;
                case 3:
                    MenuContas menuContas = new MenuContas(compraService, fornecedorService, produtoService,
                            vendaService, clienteService);
                    menuContas.exibirMenu(scanner);
                    break;
                case 4:
                    MenuRelatorios menuRelatorios = new MenuRelatorios(vendaService, produtoService, clienteService,
                            compraService, fornecedorService);
                    menuRelatorios.exibirMenu(scanner);
                    break;
                case 0:
                    System.out.println("Saindo do sistema. Até mais!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }
}