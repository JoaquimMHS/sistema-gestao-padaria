// src/main/java/org/padaria/ui/MenuVendas.java
package org.padaria.ui;

import org.padaria.model.ModoPagamento;
import org.padaria.model.Venda;
import org.padaria.service.VendaService;
import org.padaria.util.MenuUtil;

import java.time.LocalDate;
import java.util.Scanner;

public class MenuVendas {
    private final VendaService vendaService;
    private final Scanner scanner;

    public MenuVendas(VendaService vendaService, Scanner scanner) {
        this.vendaService = vendaService;
        this.scanner = scanner;
    }

    public void exibirMenu() {
        int opcao;
        do {
            System.out.println("\n--- MENU DE VENDAS (Console) ---");
            System.out.println("1. Registrar Nova Venda");
            System.out.println("2. Listar Todas as Vendas");
            System.out.println("3. Buscar Venda por Código");
            System.out.println("4. Vendas por Cliente");
            System.out.println("5. Vendas por Produto");
            System.out.println("6. Vendas por Forma de Pagamento");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            opcao = MenuUtil.lerOpcao(scanner);

            switch (opcao) {
                case 1: registrarVenda(); break;
                case 2: listarVendas(); break;
                case 3: buscarVenda(); break;
                case 4: listarVendasCliente(); break;
                case 5: listarVendasProduto(); break;
                case 6: listarVendasPagamento(); break;
                case 0: System.out.println("Voltando ao Menu Principal."); break;
                default: System.out.println("Opção inválida. Tente novamente."); break;
            }
        } while (opcao != 0);
    }

    public void registrarVenda() {
        System.out.print("Data da Venda (YYYY-MM-DD): ");
        LocalDate data = LocalDate.parse(scanner.nextLine());
        System.out.print("Código do Produto: ");
        int codigoProduto = Integer.parseInt(scanner.nextLine());
        System.out.print("Quantidade: ");
        int quantidade = Integer.parseInt(scanner.nextLine());
        System.out.print("Modo de Pagamento ($/X/D/C/T/F): ");
        ModoPagamento modo = ModoPagamento.fromCaracter(scanner.nextLine().charAt(0));

        Integer codigoCliente = null;
        if (modo == ModoPagamento.FIADO) {
            System.out.print("Código do Cliente: ");
            codigoCliente = Integer.parseInt(scanner.nextLine());
        }

        Venda novaVenda = new Venda(codigoCliente, data, codigoProduto, quantidade, modo);
        vendaService.cadastrar(novaVenda);
        System.out.println("Venda registrada com sucesso!");
    }

    public void listarVendas() {
        vendaService.listar().forEach(System.out::println);
    }

    public void buscarVenda() {
        System.out.print("Digite o código da venda: ");
        int codigo = Integer.parseInt(scanner.nextLine());
        Venda venda = vendaService.buscar(codigo);
        if (venda != null) {
            System.out.println(venda);
        } else {
            System.out.println("Venda não encontrada.");
        }
    }

    public void listarVendasCliente() {
        System.out.print("Digite o código do cliente: ");
        int codigoCliente = Integer.parseInt(scanner.nextLine());
        vendaService.listarVendasPorCliente(codigoCliente).forEach(System.out::println);
    }

    public void listarVendasProduto() {
        System.out.print("Digite o código do produto: ");
        int codigoProduto = Integer.parseInt(scanner.nextLine());
        vendaService.listarVendasPorProduto(codigoProduto).forEach(System.out::println);
    }

    public void listarVendasPagamento() {
        System.out.print("Digite o caractere do modo de pagamento ($/X/D/C/T/F): ");
        ModoPagamento modo = ModoPagamento.fromCaracter(scanner.nextLine().charAt(0));
        vendaService.listarVendasPorPagamento(modo).forEach(System.out::println);
    }
}