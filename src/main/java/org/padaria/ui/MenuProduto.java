package org.padaria.ui;

import org.padaria.model.Produto;
import org.padaria.service.ProdutoService;
import org.padaria.util.MenuUtil;

import java.util.List;
import java.util.Scanner;
import java.util.Collections;

public class MenuProduto {

    private final ProdutoService produtoService;

    public MenuProduto(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    public void exibirMenu(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n--- MENU DE PRODUTOS ---");
            System.out.println("1. Listar Produtos");
            System.out.println("2. Adicionar Produto");
            System.out.println("3. Editar Produto");
            System.out.println("4. Remover Produto");
            System.out.println("5. Pesquisar Produto por Código");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = MenuUtil.lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    listarProdutos(produtoService.listar());
                    break;
                case 2:
                    adicionarProduto(scanner);
                    break;
                case 3:
                    editarProduto(scanner);
                    break;
                case 4:
                    removerProduto(scanner);
                    break;
                case 5:
                    pesquisarProduto(scanner);
                    break;
                case 0:
                    System.out.println("Voltando ao menu anterior.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }

    private void listarProdutos(List<Produto> produtos) {
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }
        System.out.println("\n--- LISTA DE PRODUTOS ---");
        System.out.printf("%-5s | %-25s | %-10s | %-10s | %-15s | %-15s%n",
                "Cód", "Descrição", "Est. Atual", "Est. Mín", "Valor Custo", "Valor Venda");
        System.out.println("--------------------------------------------------------------------------------------");
        for (Produto p : produtos) {
            System.out.printf("%-5d | %-25s | %-10d | %-10d | R$ %-11.2f | R$ %-11.2f%n",
                    p.getCodigo(), p.getDescricao(), p.getEstoqueAtual(), p.getEstoqueMinimo(),
                    p.getValorCusto(), p.calcularValorVenda());
        }
    }

    private void adicionarProduto(Scanner scanner) {
        System.out.println("\n--- ADICIONAR NOVO PRODUTO ---");
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();
        System.out.print("Estoque Atual: ");
        int estoqueAtual = MenuUtil.lerOpcao(scanner);
        System.out.print("Estoque Mínimo: ");
        int estoqueMinimo = MenuUtil.lerOpcao(scanner);
        System.out.print("Valor de Custo: ");
        double valorCusto = MenuUtil.lerDouble(scanner);
        System.out.print("Percentual de Lucro (%): ");
        int percentualLucro = MenuUtil.lerOpcao(scanner);

        Produto novoProduto = new Produto();
        novoProduto.setDescricao(descricao);
        novoProduto.setEstoqueAtual(estoqueAtual);
        novoProduto.setEstoqueMinimo(estoqueMinimo);
        novoProduto.setValorCusto(valorCusto);
        novoProduto.setPercentualLucro(percentualLucro);

        produtoService.cadastrar(novoProduto);
        System.out.println("Produto adicionado com sucesso!");
    }

    private void editarProduto(Scanner scanner) {
        System.out.println("\n--- EDITAR PRODUTO ---");
        System.out.print("Digite o código do produto para editar: ");
        int codigo = MenuUtil.lerOpcao(scanner);
        Produto produtoParaEditar = produtoService.buscar(codigo);

        if (produtoParaEditar == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.println("Produto encontrado: " + produtoParaEditar.getDescricao());
        System.out.print("Nova descrição (" + produtoParaEditar.getDescricao() + "): ");
        String descricao = scanner.nextLine();
        if (!descricao.isEmpty()) {
            produtoParaEditar.setDescricao(descricao);
        }

        System.out.print("Novo estoque atual (" + produtoParaEditar.getEstoqueAtual() + "): ");
        String estoqueAtualStr = scanner.nextLine();
        if (!estoqueAtualStr.isEmpty()) {
            produtoParaEditar.setEstoqueAtual(Integer.parseInt(estoqueAtualStr));
        }

        System.out.print("Novo estoque mínimo (" + produtoParaEditar.getEstoqueMinimo() + "): ");
        String estoqueMinimoStr = scanner.nextLine();
        if (!estoqueMinimoStr.isEmpty()) {
            produtoParaEditar.setEstoqueMinimo(Integer.parseInt(estoqueMinimoStr));
        }

        System.out.print("Novo valor de custo (" + produtoParaEditar.getValorCusto() + "): ");
        String valorCustoStr = scanner.nextLine();
        if (!valorCustoStr.isEmpty()) {
            produtoParaEditar.setValorCusto(Double.parseDouble(valorCustoStr));
        }

        System.out.print("Novo percentual de lucro (" + produtoParaEditar.getPercentualLucro() + "): ");
        String percentualLucroStr = scanner.nextLine();
        if (!percentualLucroStr.isEmpty()) {
            produtoParaEditar.setPercentualLucro(Integer.parseInt(percentualLucroStr));
        }

        produtoService.atualizar(produtoParaEditar);
        System.out.println("Produto atualizado com sucesso!");
    }

    private void removerProduto(Scanner scanner) {
        System.out.println("\n--- REMOVER PRODUTO ---");
        System.out.print("Digite o código do produto para remover: ");
        int codigo = MenuUtil.lerOpcao(scanner);

        System.out.print("Tem certeza que deseja remover o produto de código " + codigo + "? (S/N): ");
        String confirmacao = scanner.nextLine();
        if (confirmacao.equalsIgnoreCase("S")) {
            if (produtoService.remover(codigo)) {
                System.out.println("Produto removido com sucesso!");
            } else {
                System.out.println("Não foi possível remover o produto. Código não encontrado.");
            }
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    private void pesquisarProduto(Scanner scanner) {
        System.out.println("\n--- PESQUISAR PRODUTO ---");
        System.out.print("Digite o código do produto: ");
        try {
            int codigo = MenuUtil.lerOpcao(scanner);
            Produto produtoEncontrado = produtoService.buscar(codigo);
            if (produtoEncontrado != null) {
                listarProdutos(Collections.singletonList(produtoEncontrado));
            } else {
                System.out.println("Produto não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Código inválido. Por favor, digite um número inteiro.");
        }
    }
}