// src/main/java/org/padaria/view/TelaRelatorios.java
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
import org.padaria.report.RelatorioVendasPagamento;
import org.padaria.report.RelatorioVendasProduto;
import org.padaria.service.ProdutoService;
import org.padaria.service.VendaService;

public class TelaRelatorios extends JFrame {

    private final JFrame parent;
    private final VendaService vendaService;
    private final ProdutoService produtoService;

    private JButton btnVendasProduto;
    private JButton btnVendasPagamento;

    public TelaRelatorios(JFrame parent) {
        this.parent = parent;
        this.vendaService = new VendaService();
        this.produtoService = new ProdutoService();

        try {
            vendaService.carregarDados("vendas.csv");
            produtoService.carregarDados("produtos.csv");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados para os relatórios: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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

        btnVendasProduto = new JButton("Vendas e Lucro por Produto");
        btnVendasPagamento = new JButton("Vendas e Lucro por Forma de Pagamento");

        panel.add(titulo);
        panel.add(btnVendasProduto);
        panel.add(btnVendasPagamento);

        add(panel);
    }

    private void adicionarListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                parent.setVisible(true);
            }
        });

        btnVendasProduto.addActionListener(e -> {
            try {
                RelatorioVendasProduto relatorio = new RelatorioVendasProduto(vendaService, produtoService);
                List<String[]> dados = relatorio.processarDados();
                String[] cabecalho = relatorio.getCabecalho().split(";");
                new TelaVisualizacaoRelatorio(this, "Relatório de Vendas por Produto", dados, cabecalho, relatorio).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao processar dados do relatório: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnVendasPagamento.addActionListener(e -> {
            try {
                RelatorioVendasPagamento relatorio = new RelatorioVendasPagamento(vendaService, produtoService);
                List<String[]> dados = relatorio.processarDados();
                String[] cabecalho = relatorio.getCabecalho().split(";");
                new TelaVisualizacaoRelatorio(this, "Relatório de Vendas por Pagamento", dados, cabecalho, relatorio).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao processar dados do relatório: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}