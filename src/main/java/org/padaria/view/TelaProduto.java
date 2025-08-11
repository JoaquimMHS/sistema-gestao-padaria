// Salve em: org/padaria/view/TelaProduto.java
package org.padaria.view;

import org.padaria.model.Produto;
import org.padaria.service.ProdutoService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.List;

public class TelaProduto extends JFrame {

    private final ProdutoService produtoService;
    private final String arquivoCSV = "produtos.csv";

    // Componentes da Interface
    private JTable tabelaProdutos;
    private DefaultTableModel tableModel;
    private JButton btnAdicionar, btnEditar, btnRemover;
    private JTextField txtPesquisar;

    public TelaProduto() {
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
        // ... (Nenhuma alteração nesta parte do código)
        // --- Painel do Topo (Pesquisa e Botão Adicionar) ---
        JPanel panelTopo = new JPanel(new BorderLayout(10, 10));
        panelTopo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtPesquisar = new JTextField();
        txtPesquisar.setBorder(BorderFactory.createTitledBorder("Pesquisar por Código"));
        panelTopo.add(txtPesquisar, BorderLayout.CENTER);

        btnAdicionar = new JButton("Adicionar Produto +");
        btnAdicionar.setBackground(new Color(21, 139, 21));
        btnAdicionar.setForeground(Color.WHITE);
        panelTopo.add(btnAdicionar, BorderLayout.EAST);

        // --- Tabela de Produtos (Centro) ---
        String[] colunas = {"Código", "Descrição", "Estoque Atual", "Estoque Mínimo", "Valor de Custo", "Valor de Venda"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaProdutos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);

        // --- Painel de Botões Inferior (Editar e Remover) ---
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnEditar = new JButton("Editar Selecionado");
        btnRemover = new JButton("Remover Selecionado");
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnRemover);

        // --- Adiciona os painéis à janela ---
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
            }
        });

        btnAdicionar.addActionListener(e -> {
            // Abre a tela de cadastro para um NOVO produto (passando null)
            TelaCadastroProduto telaCadastro = new TelaCadastroProduto(this, produtoService, this::atualizarTabela, null);
            telaCadastro.setVisible(true);
        });

        // --- NOVO: Listener do botão Editar ---
        btnEditar.addActionListener(e -> editarProduto());

        btnRemover.addActionListener(e -> removerProduto());

        // --- NOVO: Listener do campo de Pesquisa ---
        txtPesquisar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filtrarTabela(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filtrarTabela(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filtrarTabela(); }
        });
    }

    // O método agora aceita uma lista para poder exibir a lista completa ou a filtrada
    private void carregarDadosNaTabela(List<Produto> produtos) {
        tableModel.setRowCount(0);
        for (Produto p : produtos) {
            tableModel.addRow(new Object[]{
                    p.getCodigo(),
                    p.getDescricao(),
                    p.getEstoqueAtual(),
                    p.getEstoqueMinimo(),
                    String.format("R$ %.2f", p.getValorCusto()),
                    String.format("R$ %.2f", p.calcularValorVenda())
            });
        }
    }

    // Método de callback para simplificar a atualização da tabela
    public void atualizarTabela() {
        carregarDadosNaTabela(produtoService.listar());
    }

    // --- NOVO: Método para filtrar a tabela com base na pesquisa ---
    private void filtrarTabela() {
        String textoBusca = txtPesquisar.getText().trim();
        if (textoBusca.isEmpty()) {
            atualizarTabela(); // Se a busca estiver vazia, mostra todos os produtos
        } else {
            try {
                int codigo = Integer.parseInt(textoBusca);
                Produto produtoEncontrado = produtoService.buscar(codigo);
                if (produtoEncontrado != null) {
                    // Se encontrou, mostra uma lista contendo apenas esse produto
                    carregarDadosNaTabela(Collections.singletonList(produtoEncontrado));
                } else {
                    tableModel.setRowCount(0); // Se não encontrou, limpa a tabela
                }
            } catch (NumberFormatException e) {
                tableModel.setRowCount(0); // Se o texto não for um número válido, limpa a tabela
            }
        }
    }

    // --- NOVO: Método para abrir a janela de edição ---
    private void editarProduto() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um produto na tabela para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int codigo = (int) tableModel.getValueAt(selectedRow, 0);
        Produto produtoParaEditar = produtoService.buscar(codigo);

        // Abre a mesma tela de cadastro, mas agora passando o produto a ser editado
        TelaCadastroProduto telaEdicao = new TelaCadastroProduto(this, produtoService, this::atualizarTabela, produtoParaEditar);
        telaEdicao.setVisible(true);
    }

    private void removerProduto() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um produto na tabela para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover o produto selecionado?", "Confirmação de Remoção", JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.YES_OPTION) {
            int codigo = (int) tableModel.getValueAt(selectedRow, 0);
            produtoService.remover(codigo);
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Produto removido com sucesso!");
        }
    }
}