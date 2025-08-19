// src/main/java/org/padaria/view/TelaVisualizacaoRelatorio.java
package org.padaria.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.io.IOException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.padaria.report.IRelatorio;

public class TelaVisualizacaoRelatorio extends JDialog {

    private final List<String[]> dadosRelatorio;
    private final String[] cabecalho;
    private final IRelatorio relatorio;

    private JTable tabela;
    private DefaultTableModel tableModel;
    private JButton btnSalvar;

    public TelaVisualizacaoRelatorio(Frame owner, String titulo, List<String[]> dados, String[] cabecalho,
            IRelatorio relatorio) {
        super(owner, titulo, true);
        this.dadosRelatorio = dados;
        this.cabecalho = cabecalho;
        this.relatorio = relatorio;

        setSize(800, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(cabecalho, 0);
        tabela = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabela);

        preencherTabela();

        JPanel panelBotao = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSalvar = new JButton("Salvar como CSV");
        panelBotao.add(btnSalvar);

        add(scrollPane, BorderLayout.CENTER);
        add(panelBotao, BorderLayout.SOUTH);

        adicionarListeners();
    }

    private void preencherTabela() {
        for (String[] linha : dadosRelatorio) {
            tableModel.addRow(linha);
        }
    }

    private void adicionarListeners() {
        btnSalvar.addActionListener(e -> {
            try {
                String nomeArquivo = "";
                if (relatorio instanceof org.padaria.report.RelatorioContasPagar) {
                    nomeArquivo = "1-apagar.csv";
                } else if (relatorio instanceof org.padaria.report.RelatorioContasReceber) {
                    nomeArquivo = "2-areceber.csv";
                } else if (relatorio instanceof org.padaria.report.RelatorioVendasProduto) {
                    nomeArquivo = "3-vendasprod.csv";
                } else if (relatorio instanceof org.padaria.report.RelatorioVendasPagamento) {
                    nomeArquivo = "4-vendaspgto.csv";
                } else if (relatorio instanceof org.padaria.report.RelatorioEstoque) {
                    nomeArquivo = "5-estoque.csv";
                }

                if (!nomeArquivo.isEmpty()) {
                    relatorio.gerar(nomeArquivo);
                    JOptionPane.showMessageDialog(this, "Relatório salvo com sucesso em: " + nomeArquivo, "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Tipo de relatório não reconhecido.", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar o arquivo: " + ex.getMessage(), "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}