package org.padaria.ui;

import org.padaria.model.Cliente;
import org.padaria.model.PessoaFisica;
import org.padaria.model.PessoaJuridica;
import org.padaria.service.ClienteService;
import org.padaria.util.MenuUtil;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MenuCliente {

    private final ClienteService clienteService;

    public MenuCliente(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void exibirMenu(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n--- MENU DE CLIENTES ---");
            System.out.println("1. Listar Todos os Clientes");
            System.out.println("2. Adicionar Novo Cliente");
            System.out.println("3. Editar Cliente");
            System.out.println("4. Remover Cliente");
            System.out.println("5. Pesquisar Cliente por Nome");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = MenuUtil.lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    listarClientes();
                    break;
                case 2:
                    cadastrarCliente(scanner);
                    break;
                case 3:
                    editarCliente(scanner);
                    break;
                case 4:
                    removerCliente(scanner);
                    break;
                case 5:
                    pesquisarCliente(scanner);
                    break;
                case 0:
                    System.out.println("Voltando ao menu anterior.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }

    private void listarClientes() {
        List<Cliente> clientes = clienteService.listar();
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        System.out.println("\n--- LISTA DE CLIENTES ---");
        System.out.printf("%-5s | %-25s | %-15s | %-20s | %-15s | %-30s%n",
                "Cód", "Nome", "Tipo", "Documento", "Telefone", "Endereço");
        System.out.println(
                "-------------------------------------------------------------------------------------------------------------------");
        for (Cliente c : clientes) {
            String tipo = (c instanceof PessoaFisica) ? "Pessoa Física" : "Pessoa Jurídica";
            String documento = (c instanceof PessoaFisica) ? ((PessoaFisica) c).getCpf()
                    : ((PessoaJuridica) c).getCnpj();
            System.out.printf("%-5d | %-25s | %-15s | %-20s | %-15s | %-30s%n",
                    c.getCodigo(), c.getNome(), tipo, documento, c.getTelefone(), c.getEndereco());
        }
    }

    private void cadastrarCliente(Scanner scanner) {
        System.out.println("\n--- CADASTRAR NOVO CLIENTE ---");
        System.out.print("Tipo (1 - Física, 2 - Jurídica): ");
        int tipo = MenuUtil.lerOpcao(scanner);

        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        Cliente novoCliente;
        if (tipo == 1) {
            System.out.print("CPF: ");
            String cpf = scanner.nextLine();

            PessoaFisica novaPessoaFisica = new PessoaFisica();
            novaPessoaFisica.setNome(nome);
            novaPessoaFisica.setEndereco(endereco);
            novaPessoaFisica.setTelefone(telefone);
            novaPessoaFisica.setCpf(cpf);
            novoCliente = novaPessoaFisica;
        } else if (tipo == 2) {
            System.out.print("CNPJ: ");
            String cnpj = scanner.nextLine();

            PessoaJuridica novaPessoaJuridica = new PessoaJuridica();
            novaPessoaJuridica.setNome(nome);
            novaPessoaJuridica.setEndereco(endereco);
            novaPessoaJuridica.setTelefone(telefone);
            novaPessoaJuridica.setCnpj(cnpj);
            novoCliente = novaPessoaJuridica;
        } else {
            System.out.println("Tipo inválido. O cliente não foi cadastrado.");
            return;
        }

        clienteService.cadastrar(novoCliente);
        System.out.println("Cliente cadastrado com sucesso!");
    }

    private void editarCliente(Scanner scanner) {
        System.out.println("\n--- EDITAR CLIENTE ---");
        System.out.print("Digite o código do cliente para editar: ");
        int codigo = MenuUtil.lerOpcao(scanner);
        Cliente clienteParaEditar = clienteService.buscar(codigo);

        if (clienteParaEditar == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        System.out.println("Cliente encontrado: " + clienteParaEditar.getNome());
        System.out.print("Novo nome (" + clienteParaEditar.getNome() + "): ");
        String nome = scanner.nextLine();
        if (!nome.isEmpty()) {
            clienteParaEditar.setNome(nome);
        }

        System.out.print("Novo endereço (" + clienteParaEditar.getEndereco() + "): ");
        String endereco = scanner.nextLine();
        if (!endereco.isEmpty()) {
            clienteParaEditar.setEndereco(endereco);
        }

        System.out.print("Novo telefone (" + clienteParaEditar.getTelefone() + "): ");
        String telefone = scanner.nextLine();
        if (!telefone.isEmpty()) {
            clienteParaEditar.setTelefone(telefone);
        }

        clienteService.atualizar(clienteParaEditar);
        System.out.println("Cliente atualizado com sucesso!");
    }

    private void removerCliente(Scanner scanner) {
        System.out.println("\n--- REMOVER CLIENTE ---");
        System.out.print("Digite o código do cliente para remover: ");
        int codigo = MenuUtil.lerOpcao(scanner);

        System.out.print("Tem certeza que deseja remover o cliente de código " + codigo + "? (S/N): ");
        String confirmacao = scanner.nextLine();
        if (confirmacao.equalsIgnoreCase("S")) {
            if (clienteService.remover(codigo)) {
                System.out.println("Cliente removido com sucesso!");
            } else {
                System.out.println("Não foi possível remover o cliente. Código não encontrado.");
            }
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    private void pesquisarCliente(Scanner scanner) {
        System.out.println("\n--- PESQUISAR CLIENTE ---");
        System.out.print("Digite o nome ou parte do nome do cliente: ");
        String textoBusca = scanner.nextLine().trim().toLowerCase();

        List<Cliente> clientesFiltrados = clienteService.listar().stream()
                .filter(c -> c.getNome().toLowerCase().contains(textoBusca))
                .collect(Collectors.toList());

        if (clientesFiltrados.isEmpty()) {
            System.out.println("Nenhum cliente encontrado com este nome.");
        } else {
            System.out.println("\n--- RESULTADOS DA PESQUISA ---");
            listarClientes(clientesFiltrados);
        }
    }

    private void listarClientes(List<Cliente> clientes) {
        System.out.printf("%-5s | %-25s | %-15s | %-20s | %-15s | %-30s%n",
                "Cód", "Nome", "Tipo", "Documento", "Telefone", "Endereço");
        System.out.println(
                "-------------------------------------------------------------------------------------------------------------------");
        for (Cliente c : clientes) {
            String tipo = (c instanceof PessoaFisica) ? "Pessoa Física" : "Pessoa Jurídica";
            String documento = (c instanceof PessoaFisica) ? ((PessoaFisica) c).getCpf()
                    : ((PessoaJuridica) c).getCnpj();
            System.out.printf("%-5d | %-25s | %-15s | %-20s | %-15s | %-30s%n",
                    c.getCodigo(), c.getNome(), tipo, documento, c.getTelefone(), c.getEndereco());
        }
    }
}