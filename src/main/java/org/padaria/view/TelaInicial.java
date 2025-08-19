package org.padaria.view;

import javax.swing.*;

import org.padaria.service.ClienteService;
import org.padaria.service.CompraService;
import org.padaria.service.FornecedorService;
import org.padaria.service.ProdutoService;
import org.padaria.service.VendaService;

import java.awt.*;

public class TelaInicial extends JFrame {

    private final CompraService compraService = new CompraService();
    private final FornecedorService fornecedorService = new FornecedorService();
    private final ProdutoService produtoService = new ProdutoService();
    private final VendaService vendaService = new VendaService();
    private final String clientesCSV = "clientes.csv";
    private final ClienteService clienteService = new ClienteService(clientesCSV);

    public TelaInicial() {
        setTitle("Sistema Padaria");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        try {
            compraService.carregarDados("compras.csv");
            fornecedorService.carregarDados("fornecedores.csv");
            produtoService.carregarDados("produtos.csv");
            vendaService.carregarDados("vendas.csv");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }

        // Mantém o título no topo
        JLabel titulo = new JLabel("Sistema Padaria", SwingConstants.CENTER);
        titulo.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        // Cria e configura a barra de menus
        configurarMenuBar();

        // Painel central com a mensagem de boas-vindas
        JLabel mensagem = new JLabel(
                "<html><div style='text-align: center;'>Bem vindo ao Sistema<br/>feito por Eliaquim, Felicio, Joaquim, João Pedro e Nicolas</div></html>",
                SwingConstants.CENTER);
        mensagem.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.add(mensagem, BorderLayout.CENTER);

        add(homePanel, BorderLayout.CENTER);
    }

    private void configurarMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // --- Menu Cadastro ---
        JMenu menuCadastro = new JMenu("Cadastro");
        JMenuItem itemClientes = new JMenuItem("Clientes");
        JMenuItem itemFornecedores = new JMenuItem("Fornecedores");
        JMenuItem itemProdutos = new JMenuItem("Produtos");

        itemClientes.addActionListener(e -> new TelaCliente(this).setVisible(true));
        itemFornecedores.addActionListener(e -> new TelaFornecedor(this).setVisible(true));
        itemProdutos.addActionListener(e -> new TelaProduto(this).setVisible(true));

        menuCadastro.add(itemClientes);
        menuCadastro.add(itemFornecedores);
        menuCadastro.add(itemProdutos);

        // --- Menu Registro de Vendas ---
        JMenu menuVendas = new JMenu("Registro de Vendas");
        JMenuItem itemRegistrarVenda = new JMenuItem("Gerenciar Vendas");

        itemRegistrarVenda.addActionListener(e -> new TelaVendas(this).setVisible(true));
        menuVendas.add(itemRegistrarVenda);
        menuBar.add(menuVendas);

        // --- Menu Controle de Contas ---
        JMenu menuContas = new JMenu("Controle de Contas");
        JMenuItem itemRegistrarCompra = new JMenuItem("Gerenciar Compras");
        JMenuItem itemContasPagar = new JMenuItem("Visualizar Contas a Pagar (Compras)");
        JMenuItem itemContasReceber = new JMenuItem("Visualizar Contas a Receber (Clientes)");

        itemRegistrarCompra.addActionListener(e -> new TelaCompras(this).setVisible(true));
        itemContasPagar.addActionListener(
                e -> new TelaContasAPagar(this, compraService, fornecedorService, produtoService).setVisible(true));
        itemContasReceber.addActionListener(e -> {
            TelaContasAReceber tela = new TelaContasAReceber(
                    this,
                    vendaService,
                    clienteService,
                    produtoService);
            tela.setVisible(true);
        });

        menuContas.add(itemRegistrarCompra);
        menuContas.add(itemContasPagar);
        menuContas.add(itemContasReceber);

        JMenu menuRelatorios = new JMenu("Geração de Relatórios Mensais");
        JMenuItem itemGerarRelatorios = new JMenuItem("Abrir Tela de Relatórios");

        itemGerarRelatorios.addActionListener(e -> new TelaRelatorios(this).setVisible(true));

        menuRelatorios.add(itemGerarRelatorios);

        // --- Menu Sistema (para a opção Sair) ---
        JMenu menuSistema = new JMenu("Sistema");
        JMenuItem itemSair = new JMenuItem("Sair");

        itemSair.addActionListener(e -> System.exit(0));

        menuSistema.add(itemSair);

        // Adiciona os menus à barra de menus
        menuBar.add(menuCadastro);
        menuBar.add(menuVendas);
        menuBar.add(menuContas);
        menuBar.add(menuRelatorios);
        menuBar.add(menuSistema);

        // Define a barra de menus para a janela
        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaInicial().setVisible(true));
    }
}