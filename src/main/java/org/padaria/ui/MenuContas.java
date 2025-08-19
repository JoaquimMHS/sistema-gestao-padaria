package org.padaria.ui;

import org.padaria.service.ClienteService;
import org.padaria.service.CompraService;
import org.padaria.service.FornecedorService;
import org.padaria.service.ProdutoService;
import org.padaria.service.VendaService;
import org.padaria.util.MenuUtil;

import java.util.Scanner;

public class MenuContas {

    private final CompraService compraService;
    private final FornecedorService fornecedorService;
    private final ProdutoService produtoService;
    private final VendaService vendaService;
    private final ClienteService clienteService;

    public MenuContas(CompraService compraService, FornecedorService fornecedorService, ProdutoService produtoService,
            VendaService vendaService, ClienteService clienteService) {
        this.compraService = compraService;
        this.fornecedorService = fornecedorService;
        this.produtoService = produtoService;
        this.vendaService = vendaService;
        this.clienteService = clienteService;
    }

    public void exibirMenu(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n--- MENU DE CONTAS ---");
            System.out.println("1. Gerenciar Compras");
            System.out.println("2. Visualizar Contas a Pagar");
            System.out.println("3. Visualizar Contas a Receber");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = MenuUtil.lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    MenuCompras menuCompras = new MenuCompras(compraService, fornecedorService, produtoService);
                    menuCompras.exibirMenu(scanner);
                    break;
                case 2:
                    MenuContasPagar menuContasPagar = new MenuContasPagar(compraService, fornecedorService,
                            produtoService);
                    menuContasPagar.exibirMenu(scanner);
                    break;
                case 3:
                    MenuContasReceber menuContasReceber = new MenuContasReceber(vendaService, clienteService,
                            produtoService);
                    menuContasReceber.exibirMenu(scanner);
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