// src/main/java/org/padaria/view/TelaCompras.java
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
import org.padaria.model.Compra;
import org.padaria.service.CompraService;

public class TelaCompras extends JFrame {

    private final JFrame parent;
    private final CompraService compraService;
    private final String arquivoCSV = "compras.csv";

    private JTable tabelaCompras;
    private DefaultTableModel tableModel;
    private JButton btnAdicionar, btnEditar, btnRemover, btnVoltar;
    private JTextField txtPesquisar;

    public TelaCompras(JFrame parent) {
        this.parent = parent;
        this.compraService = new CompraService();

        try {
            compraService.carregarDados(arquivoCSV);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados de compras: " + e.getMessage(), "Erro de Carga", JOptionPane.ERROR_MESSAGE);
        }

        setTitle("Gestão de Compras");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        adicionarListeners();

        carregarDadosNaTabela(compraService.listar());
    }

    private void inicializarComponentes() {
        JPanel panelTopo = new JPanel(new BorderLayout(10, 10));
        panelTopo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnVoltar = new JButton("<- Voltar");
        panelTopo.add(btnVoltar, BorderLayout.WEST);

        txtPesquisar = new JTextField();
        txtPesquisar.setBorder(BorderFactory.createTitledBorder("Pesquisar por NF"));
        panelTopo.add(txtPesquisar, BorderLayout.CENTER);

        btnAdicionar = new JButton("Adicionar Compra +");
        panelTopo.add(btnAdicionar, BorderLayout.EAST);

        String[] colunas = {"Nº NF", "Cód. Fornecedor", "Data Compra", "Cód. Produto", "Quantidade"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaCompras = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaCompras);

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
            TelaCadastroCompra telaCadastro = new TelaCadastroCompra(this, compraService, this::atualizarTabela, null);
            telaCadastro.setVisible(true);
        });

        btnEditar.addActionListener(e -> editarCompra());
        btnRemover.addActionListener(e -> removerCompra());

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
            compraService.salvarDados(arquivoCSV);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar dados de compras: " + ex.getMessage(), "Erro de Salvamento", JOptionPane.ERROR_MESSAGE);
        }
        parent.setVisible(true);
        dispose();
    }

    private void carregarDadosNaTabela(List<Compra> compras) {
        tableModel.setRowCount(0);
        for (Compra c : compras) {
            tableModel.addRow(new Object[]{
                    c.getNumeroNotaFiscal(), c.getCodigoFornecedor(), c.getDataCompra(), c.getCodigoProduto(), c.getQuantidade()
            });
        }
    }

    public void atualizarTabela() {
        carregarDadosNaTabela(compraService.listar());
        txtPesquisar.setText("");
    }

    private void filtrarTabela() {
        String textoBusca = txtPesquisar.getText().trim();
        if (textoBusca.isEmpty()) {
            atualizarTabela();
        } else {
            try {
                int codigo = Integer.parseInt(textoBusca);
                Compra compraEncontrada = compraService.buscar(codigo);
                carregarDadosNaTabela(compraEncontrada != null ? Collections.singletonList(compraEncontrada) : Collections.emptyList());
            } catch (NumberFormatException e) {
                tableModel.setRowCount(0);
            }
        }
    }

    private void editarCompra() {
        int selectedRow = tabelaCompras.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma compra na tabela para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int codigo = (int) tableModel.getValueAt(selectedRow, 0);
        Compra compraParaEditar = compraService.buscar(codigo);
        TelaCadastroCompra telaEdicao = new TelaCadastroCompra(this, compraService, this::atualizarTabela, compraParaEditar);
        telaEdicao.setVisible(true);
    }
    private void removerCompra() {
        int selectedRow = tabelaCompras.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma compra na tabela para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover a compra selecionada?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.YES_OPTION) {
            int codigo = (int) tableModel.getValueAt(selectedRow, 0);
            compraService.remover(codigo);
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Compra removida com sucesso!");
        }
    }
}