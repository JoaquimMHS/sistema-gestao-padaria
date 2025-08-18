// src/main/java/org/padaria/view/TelaVendas.java
package org.padaria.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import org.padaria.model.Venda;
import org.padaria.service.VendaService;

public class TelaVendas extends JFrame {

    private final JFrame parent;
    private final VendaService vendaService;
    private final String arquivoCSV = "vendas.csv";

    private JTable tabelaVendas;
    private DefaultTableModel tableModel;
    private JButton btnAdicionar, btnEditar, btnRemover, btnVoltar;
    private JTextField txtPesquisar;

    public TelaVendas(JFrame parent) {
        this.parent = parent;
        this.vendaService = new VendaService();

        try {
            vendaService.carregarDados(arquivoCSV);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados de vendas: " + e.getMessage(), "Erro de Carga", JOptionPane.ERROR_MESSAGE);
        }

        setTitle("Gestão de Vendas");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        adicionarListeners();

        carregarDadosNaTabela(vendaService.listar());
    }

    private void inicializarComponentes() {
        JPanel panelTopo = new JPanel(new BorderLayout(10, 10));
        panelTopo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnVoltar = new JButton("<- Voltar");
        panelTopo.add(btnVoltar, BorderLayout.WEST);

        txtPesquisar = new JTextField();
        txtPesquisar.setBorder(BorderFactory.createTitledBorder("Pesquisar por Código"));
        panelTopo.add(txtPesquisar, BorderLayout.CENTER);

        btnAdicionar = new JButton("Adicionar Venda +");
        panelTopo.add(btnAdicionar, BorderLayout.EAST);

        String[] colunas = {"Código", "Cód. Cliente", "Data Venda", "Cód. Produto", "Quantidade", "Pagamento"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaVendas = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaVendas);

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
                salvarEFechar();
            }
        });

        btnVoltar.addActionListener(e -> {
            salvarEFechar();
        });

        btnAdicionar.addActionListener(e -> {
            TelaCadastroVenda telaCadastro = new TelaCadastroVenda(this, vendaService, this::atualizarTabela, null);
            telaCadastro.setVisible(true);
        });

        btnEditar.addActionListener(e -> editarVenda());
        btnRemover.addActionListener(e -> removerVenda());

        txtPesquisar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filtrarTabela(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filtrarTabela(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filtrarTabela(); }
        });
    }

    private void salvarEFechar() {
        try {
            vendaService.salvarDados(arquivoCSV);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar dados de vendas: " + ex.getMessage(), "Erro de Salvamento", JOptionPane.ERROR_MESSAGE);
        }
        parent.setVisible(true);
        dispose();
    }

    private void carregarDadosNaTabela(List<Venda> vendas) {
        tableModel.setRowCount(0);
        for (Venda v : vendas) {
            tableModel.addRow(new Object[]{
                    v.getCodigo(), v.getCodigoCliente(), v.getDataVenda(), v.getCodigoProduto(), v.getQuantidade(), v.getModoPagamento().name()
            });
        }
    }

    public void atualizarTabela() {
        carregarDadosNaTabela(vendaService.listar());
        txtPesquisar.setText("");
    }

    private void filtrarTabela() {
        String textoBusca = txtPesquisar.getText().trim();
        if (textoBusca.isEmpty()) {
            atualizarTabela();
        } else {
            try {
                int codigo = Integer.parseInt(textoBusca);
                Venda vendaEncontrada = vendaService.buscar(codigo);
                carregarDadosNaTabela(vendaEncontrada != null ? Collections.singletonList(vendaEncontrada) : Collections.emptyList());
            } catch (NumberFormatException e) {
                tableModel.setRowCount(0);
            }
        }
    }

    private void editarVenda() {
        int selectedRow = tabelaVendas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma venda na tabela para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int codigo = (int) tableModel.getValueAt(selectedRow, 0);
        Venda vendaParaEditar = vendaService.buscar(codigo);
        TelaCadastroVenda telaEdicao = new TelaCadastroVenda(this, vendaService, this::atualizarTabela, vendaParaEditar);
        telaEdicao.setVisible(true);
    }
    private void removerVenda() {
        // 1. Obtém o índice da linha selecionada
        int selectedRow = tabelaVendas.getSelectedRow();

        // 2. Valida se uma linha foi realmente selecionada
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma venda na tabela para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. Pede confirmação ao usuário
        int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover a venda selecionada?", "Confirmação", JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            // 4. Obtém o código da venda a partir da primeira coluna da linha selecionada
            int codigo = (int) tableModel.getValueAt(selectedRow, 0);

            // 5. Chama o serviço para remover APENAS a venda com aquele código
            vendaService.remover(codigo);

            // 6. Atualiza a tabela para refletir a remoção
            atualizarTabela();

            JOptionPane.showMessageDialog(this, "Venda removida com sucesso!");
        }
    }
}