package org.padaria.ui;

import org.padaria.model.Fornecedor;
import org.padaria.service.FornecedorService;
import org.padaria.util.MenuUtil;

import java.util.List;
import java.util.Scanner;

public class MenuFornecedor {

    private final FornecedorService fornecedorService;

    public MenuFornecedor(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    public void exibirMenu(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n--- MENU DE FORNECEDORES ---");
            System.out.println("1. Listar Fornecedores");
            System.out.println("2. Adicionar Fornecedor");
            System.out.println("3. Editar Fornecedor");
            System.out.println("4. Remover Fornecedor");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = MenuUtil.lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    listarFornecedores();
                    break;
                case 2:
                    adicionarFornecedor(scanner);
                    break;
                case 3:
                    editarFornecedor(scanner);
                    break;
                case 4:
                    removerFornecedor(scanner);
                    break;
                case 0:
                    System.out.println("Voltando ao menu anterior.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }

    private void listarFornecedores() {
        List<Fornecedor> fornecedores = fornecedorService.listar();
        if (fornecedores.isEmpty()) {
            System.out.println("Nenhum fornecedor cadastrado.");
            return;
        }
        System.out.println("\n--- LISTA DE FORNECEDORES ---");
        System.out.printf("%-5s | %-25s | %-20s | %-15s | %-25s%n",
                "Cód", "Nome", "CNPJ", "Telefone", "Pessoa de Contato");
        System.out.println("--------------------------------------------------------------------------------------");
        for (Fornecedor f : fornecedores) {
            System.out.printf("%-5d | %-25s | %-20s | %-15s | %-25s%n",
                    f.getCodigo(), f.getNome(), f.getCNPJ(), f.getTelefone(), f.getPessoaContato());
        }
    }

    private void adicionarFornecedor(Scanner scanner) {
        System.out.println("\n--- ADICIONAR NOVO FORNECEDOR ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CNPJ: ");
        String cnpj = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Pessoa de Contato: ");
        String pessoaContato = scanner.nextLine();

        Fornecedor novoFornecedor = new Fornecedor();
        novoFornecedor.setNome(nome);
        novoFornecedor.setCNPJ(cnpj);
        novoFornecedor.setTelefone(telefone);
        novoFornecedor.setPessoaContato(pessoaContato);

        fornecedorService.cadastrar(novoFornecedor);
        System.out.println("Fornecedor adicionado com sucesso!");
    }

    private void editarFornecedor(Scanner scanner) {
        System.out.println("\n--- EDITAR FORNECEDOR ---");
        System.out.print("Digite o código do fornecedor para editar: ");
        int codigo = MenuUtil.lerOpcao(scanner);
        Fornecedor fornecedorParaEditar = fornecedorService.buscar(codigo);

        if (fornecedorParaEditar == null) {
            System.out.println("Fornecedor não encontrado.");
            return;
        }

        System.out.println("Fornecedor encontrado: " + fornecedorParaEditar.getNome());
        System.out.print("Novo nome (" + fornecedorParaEditar.getNome() + "): ");
        String nome = scanner.nextLine();
        if (!nome.isEmpty()) {
            fornecedorParaEditar.setNome(nome);
        }

        System.out.print("Novo CNPJ (" + fornecedorParaEditar.getCNPJ() + "): ");
        String cnpj = scanner.nextLine();
        if (!cnpj.isEmpty()) {
            fornecedorParaEditar.setCNPJ(cnpj);
        }

        System.out.print("Novo telefone (" + fornecedorParaEditar.getTelefone() + "): ");
        String telefone = scanner.nextLine();
        if (!telefone.isEmpty()) {
            fornecedorParaEditar.setTelefone(telefone);
        }

        System.out.print("Nova pessoa de contato (" + fornecedorParaEditar.getPessoaContato() + "): ");
        String pessoaContato = scanner.nextLine();
        if (!pessoaContato.isEmpty()) {
            fornecedorParaEditar.setPessoaContato(pessoaContato);
        }

        fornecedorService.atualizar(fornecedorParaEditar);
        System.out.println("Fornecedor atualizado com sucesso!");
    }

    private void removerFornecedor(Scanner scanner) {
        System.out.println("\n--- REMOVER FORNECEDOR ---");
        System.out.print("Digite o código do fornecedor para remover: ");
        int codigo = MenuUtil.lerOpcao(scanner);

        System.out.print("Tem certeza que deseja remover o fornecedor de código " + codigo + "? (S/N): ");
        String confirmacao = scanner.nextLine();

        if (confirmacao.equalsIgnoreCase("S")) {
            fornecedorService.remover(codigo);
            System.out.println("Fornecedor removido com sucesso!");
        } else {
            System.out.println("Operação cancelada.");
        }
    }
}