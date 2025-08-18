// src/main/java/org/padaria/ui/MenuCompras.java
package org.padaria.ui;

import org.padaria.model.Compra;
import org.padaria.service.CompraService;
import java.time.LocalDate;
import java.util.Scanner;

public class MenuCompras {
    private final CompraService compraService;
    private final Scanner scanner;

    public MenuCompras(CompraService compraService, Scanner scanner) {
        this.compraService = compraService;
        this.scanner = scanner;
    }

    public void exibirMenu() {
        // ... Lógica para exibir o menu e chamar os métodos ...
    }

    public void registrarCompra() {
        System.out.print("Número da Nota Fiscal: ");
        int numeroNF = Integer.parseInt(scanner.nextLine());
        System.out.print("Código do Fornecedor: ");
        int codigoFornecedor = Integer.parseInt(scanner.nextLine());
        System.out.print("Data da Compra (YYYY-MM-DD): ");
        LocalDate data = LocalDate.parse(scanner.nextLine());
        System.out.print("Código do Produto: ");
        int codigoProduto = Integer.parseInt(scanner.nextLine());
        System.out.print("Quantidade: ");
        int quantidade = Integer.parseInt(scanner.nextLine());

        Compra novaCompra = new Compra(numeroNF, codigoFornecedor, data, codigoProduto, quantidade);
        compraService.cadastrar(novaCompra);
        System.out.println("Compra registrada com sucesso!");
    }

    public void listarCompras() {
        compraService.listar().forEach(System.out::println);
    }

    public void buscarCompra() {
        System.out.print("Digite o número da Nota Fiscal: ");
        int numeroNF = Integer.parseInt(scanner.nextLine());
        Compra compra = compraService.buscar(numeroNF);
        if (compra != null) {
            System.out.println(compra);
        } else {
            System.out.println("Compra não encontrada.");
        }
    }

    public void listarComprasFornecedor() {
        System.out.print("Digite o código do fornecedor: ");
        int codigoFornecedor = Integer.parseInt(scanner.nextLine());
        compraService.listarComprasPorFornecedor(codigoFornecedor).forEach(System.out::println);
    }
}