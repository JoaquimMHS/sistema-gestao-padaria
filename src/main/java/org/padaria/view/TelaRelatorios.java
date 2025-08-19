package org.padaria.view;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.padaria.report.RelatorioContasPagar;
import org.padaria.report.RelatorioContasReceber;
import org.padaria.report.RelatorioEstoque;
import org.padaria.report.RelatorioVendasPagamento;
import org.padaria.report.RelatorioVendasProduto;
import org.padaria.service.ClienteService;
import org.padaria.service.CompraService;
import org.padaria.service.FornecedorService;
import org.padaria.service.ProdutoService;
import org.padaria.service.VendaService;

public class TelaRelatorios extends JFrame {

    private final JFrame parent;
    private final VendaService vendaService;
    private final ProdutoService produtoService;
    private final ClienteService clienteService;
    private final CompraService compraService;
    private final FornecedorService fornecedorService;

    private JButton btnVendasPagamento, btnVendasProduto, btnAPagar, btnAReceber, btnEstoque;

    public TelaRelatorios(JFrame parent) {
        String arquivoClientes = "clientes.csv";
        this.parent = parent;
        this.vendaService = new VendaService();
        this.produtoService = new ProdutoService();
        this.clienteService = new ClienteService(arquivoClientes);
        this.compraService = new CompraService();
        this.fornecedorService = new FornecedorService();

        try {
            vendaService.carregarDados("vendas.csv");
            produtoService.carregarDados("produtos.csv");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados para os relatórios: " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }

        setTitle("Geração de Relatórios Mensais");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        adicionarListeners();
    }

    private void inicializarComponentes() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel titulo = new JLabel("Escolha o Relatório", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));

        btnAPagar = new JButton(" Total a pagar por fornecedor:");
        btnAReceber = new JButton("Total a receber por cliente:");
        btnVendasProduto = new JButton("Vendas e Lucro por Produto");
        btnVendasPagamento = new JButton("Vendas e Lucro por Forma de Pagamento");
        btnEstoque = new JButton("Estado do estoque:");

        panel.add(titulo);
        panel.add(btnAPagar);
        panel.add(btnAReceber);
        panel.add(btnVendasProduto);
        panel.add(btnVendasPagamento);
        panel.add(btnEstoque);

        add(panel);
    }

    private void adicionarListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                parent.setVisible(true);
            }
        });

        btnAPagar.addActionListener(e -> {
            try {
                // Pass the required services to the constructor
                RelatorioContasPagar relatorio = new RelatorioContasPagar(fornecedorService, compraService,
                        produtoService);
                List<String[]> dados = relatorio.processarDados();
                String[] cabecalho = relatorio.getCabecalho();
                new TelaVisualizacaoRelatorio(this, "Relatório de Contas a Pagar", dados, cabecalho, relatorio)
                        .setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao processar dados do relatório: " + ex.getMessage(), "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnAReceber.addActionListener(e -> {
            try {
                // Pass the required services to the constructor
                RelatorioContasReceber relatorio = new RelatorioContasReceber(clienteService, vendaService,
                        produtoService);
                List<String[]> dados = relatorio.processarDados();
                String[] cabecalho = relatorio.getCabecalho();
                new TelaVisualizacaoRelatorio(this, "Relatório de Contas a Receber", dados, cabecalho, relatorio)
                        .setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao processar dados do relatório: " + ex.getMessage(), "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnVendasProduto.addActionListener(e -> {
            try {
                RelatorioVendasProduto relatorio = new RelatorioVendasProduto(vendaService, produtoService);
                List<String[]> dados = relatorio.processarDados();
                String[] cabecalho = relatorio.getCabecalho();
                new TelaVisualizacaoRelatorio(this, "Relatório de Vendas por Produto", dados, cabecalho, relatorio)
                        .setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao processar dados do relatório: " + ex.getMessage(), "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnVendasPagamento.addActionListener(e -> {
            try {
                RelatorioVendasPagamento relatorio = new RelatorioVendasPagamento(vendaService, produtoService);
                List<String[]> dados = relatorio.processarDados();
                String[] cabecalho = relatorio.getCabecalho();
                new TelaVisualizacaoRelatorio(this, "Relatório de Vendas por Pagamento", dados, cabecalho, relatorio)
                        .setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao processar dados do relatório: " + ex.getMessage(), "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEstoque.addActionListener(e -> {
            try {
                // Pass the required service to the constructor
                RelatorioEstoque relatorio = new RelatorioEstoque(produtoService);
                List<String[]> dados = relatorio.processarDados();
                String[] cabecalho = relatorio.getCabecalho();
                new TelaVisualizacaoRelatorio(this, "Relatório de Estoque", dados, cabecalho, relatorio)
                        .setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao processar dados do relatório: " + ex.getMessage(), "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}