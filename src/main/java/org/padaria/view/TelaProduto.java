package org.padaria.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import org.padaria.model.Produto;
import org.padaria.report.RelatorioEstoque;
import org.padaria.service.ProdutoService;

public class TelaProduto extends JFrame {

    private final JFrame parent; 
    private final ProdutoService produtoService;
    private final String arquivoCSV = "produtos.csv";

    private JTable tabelaProdutos;
    private DefaultTableModel tableModel;
    private JButton btnAdicionar, btnEditar, btnRemover, btnVoltar;
    private JTextField txtPesquisar;

    public TelaProduto(JFrame parent) {
        this.parent = parent;
        produtoService = new ProdutoService();
        produtoService.carregarDados(arquivoCSV);

        setTitle("Produtos");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        adicionarListeners();

        carregarDadosNaTabela(produtoService.listar());
    }

    private void inicializarComponentes() {
        JPanel panelTopo = new JPanel(new BorderLayout(10, 10));
        panelTopo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        btnVoltar = new JButton("<- Voltar");
        panelTopo.add(btnVoltar, BorderLayout.WEST);

        txtPesquisar = new JTextField();
        txtPesquisar.setBorder(BorderFactory.createTitledBorder("Pesquisar por Código"));
        panelTopo.add(txtPesquisar, BorderLayout.CENTER);

        btnAdicionar = new JButton("Adicionar Produto +");
        panelTopo.add(btnAdicionar, BorderLayout.EAST);

        String[] colunas = {"Código", "Descrição", "Estoque Atual", "Estoque Mínimo", "Valor de Custo", "Valor de Venda"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaProdutos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnEditar = new JButton("Editar Selecionado");
        btnRemover = new JButton("Remover Selecionado");
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnRemover);

        setLayout(new BorderLayout());
        add(panelTopo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotoes, BorderLayout.SOUTH);
    }
    
    private void adicionarListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                produtoService.salvarDados(arquivoCSV);
                parent.setVisible(true); 
            }
        });

        btnVoltar.addActionListener(e -> {
            produtoService.salvarDados(arquivoCSV); 
            parent.setVisible(true); 
            dispose();
        });

        btnAdicionar.addActionListener(e -> {
            TelaCadastroProduto telaCadastro = new TelaCadastroProduto(this, produtoService, this::atualizarTabela, null);
            telaCadastro.setVisible(true);
        });
        btnEditar.addActionListener(e -> editarProduto());
        btnRemover.addActionListener(e -> removerProduto());
        txtPesquisar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filtrarTabela(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filtrarTabela(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filtrarTabela(); }
        });
    }

    private void carregarDadosNaTabela(List<Produto> produtos) {
        tableModel.setRowCount(0);
        for (Produto p : produtos) {
            tableModel.addRow(new Object[]{
                p.getCodigo(), p.getDescricao(), p.getEstoqueAtual(), p.getEstoqueMinimo(),
                String.format("R$ %.2f", p.getValorCusto()), String.format("R$ %.2f", p.calcularValorVenda())
            });
        }
    }

    public void atualizarTabela() {
            carregarDadosNaTabela(produtoService.listar());
        txtPesquisar.setText("");
    }

    private void filtrarTabela() {
        String textoBusca = txtPesquisar.getText().trim();
        if (textoBusca.isEmpty()) {
            atualizarTabela();
        } else {
            try {
                int codigo = Integer.parseInt(textoBusca);
                Produto produtoEncontrado = produtoService.buscar(codigo);
                carregarDadosNaTabela(produtoEncontrado != null ? Collections.singletonList(produtoEncontrado) : Collections.emptyList());
            } catch (NumberFormatException e) {
                tableModel.setRowCount(0);
            }
        }
    }


    
    private void editarProduto() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um produto na tabela para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int codigo = (int) tableModel.getValueAt(selectedRow, 0);
        Produto produtoParaEditar = produtoService.buscar(codigo);
        TelaCadastroProduto telaEdicao = new TelaCadastroProduto(this, produtoService, this::atualizarTabela, produtoParaEditar);
        telaEdicao.setVisible(true);
    }
    private void removerProduto() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um produto na tabela para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover o produto selecionado?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.YES_OPTION) {
            int codigo = (int) tableModel.getValueAt(selectedRow, 0);
            produtoService.remover(codigo);
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Produto removido com sucesso!");
        }
    }
}
